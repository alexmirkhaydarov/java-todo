package com.app.todo.controller;

import com.app.todo.model.Todo;
import com.app.todo.model.User;
import com.app.todo.repository.TodoRepository;
import com.app.todo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
public class TodoController {

    @Autowired
    TodoRepository todo_repository;
    @Autowired
    UserRepository user_repository;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(Model model) {

        Iterable<Todo> todos = todo_repository.findAll();

        model.addAttribute("todos", todos);

        return "todo/index";
    }

    @RequestMapping(value = "todo/new", method = RequestMethod.GET)
    public String newTodo(Todo todo) {
        return "todo/new";
    }

    @RequestMapping(value = "todo/create", method = RequestMethod.POST)
    public String create(Model model, @Valid @ModelAttribute Todo todo, BindingResult bindingResult) {
        User user;

        Optional<User> searched_user = user_repository.findByLogin("test_login");
        if(searched_user.isPresent())
            user = searched_user.get();
        else{
            user = new User();
            user.setLogin("test_login");
            user_repository.save(user);
        }



        if(!bindingResult.hasErrors()){
            todo.setUser(user);
            todo.setDescription(todo.getDescription()+user.getLogin());
            todo_repository.save(todo);
            Iterable<Todo> todos = todo_repository.findAll();

            model.addAttribute("todos", todos);
            return "redirect:/";
        }else{
            return "todo/new";
        }
    }

    @RequestMapping(value = "/todo/{Id}/update", method = RequestMethod.GET)
    public String updateTodo(@PathVariable int Id, Model model) {
        Optional<Todo> maybe_todo = todo_repository.findById(Id);
        Todo todo = maybe_todo.get();
        model.addAttribute("todo", todo);

        return "todo/new";
    }

    @RequestMapping(value = "/todo/{Id}", method = RequestMethod.DELETE)
    public String deleteTodo(@PathVariable int Id) {

        todo_repository.deleteById(Id);
        return "redirect:/";
    }


}
