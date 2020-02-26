package com.app.todo.services;

import com.app.todo.model.FacebookUser;
import com.app.todo.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class MyOauth2UserServiceTest {

    @Spy
    private UserRepository userRepository;// = mock(UserRepository.class);

    private MyOAuth2UserService defaultOAuth2UserServiceMock = mock(MyOAuth2UserService.class);

    @InjectMocks
    private MyOAuth2UserService myOAuth2UserService
            = new MyOAuth2UserService(defaultOAuth2UserServiceMock);

    @Before
    public void initMocks(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void whenFindByName_thenReturnEmployee() {
        OAuth2UserRequest oAuth2UserRequest = mock(OAuth2UserRequest.class);
        OAuth2User oAuth2User = mock(OAuth2User.class);
        when(defaultOAuth2UserServiceMock.loadUser(oAuth2UserRequest)).thenReturn(oAuth2User);

        myOAuth2UserService.loadUser(oAuth2UserRequest);

        verify(this.userRepository, times(1)).save(any());
    }

    @Test
    public void whenFindByName_thenReturnEmployee2() {
        OAuth2UserRequest oAuth2UserRequest = mock(OAuth2UserRequest.class);
        OAuth2User oAuth2User = mock(OAuth2User.class);
        when(oAuth2User.getAttributes()).thenReturn(Map.of("id","eqweweq", "name", "dddd"));
        when(userRepository.findByTypeAndExternalId("Facebook", "eqweweq")).thenReturn(Optional.of(mock(FacebookUser.class)));
        when(defaultOAuth2UserServiceMock.loadUser(oAuth2UserRequest)).thenReturn(oAuth2User);

        myOAuth2UserService.loadUser(oAuth2UserRequest);

        verify(this.userRepository, times(0)).save(any());
    }
}
