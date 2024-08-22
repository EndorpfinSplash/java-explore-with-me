package ru.practicum.ewm.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.ewm.user.dto.NewUserRequest;
import ru.practicum.ewm.user.dto.UserUpdateDto;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;

    private User user;


    @BeforeEach
    void setUp() {
        user = new User(1L, "test user", "testuser@mail.com");
    }

    @Test
    void getAllUsers() {
        when(userRepository.findAll()).thenReturn(Collections.emptyList());
        userService.getAllUsers();
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void createUser() {
        when(userRepository.save(any(User.class))).thenReturn(user);
        NewUserRequest newUserRequest = new NewUserRequest("creation user", "creation@mail.com");
        userService.createUser(newUserRequest);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void updateUser() {
        UserUpdateDto userUpdateDto = new UserUpdateDto("new name", "new@email.com");
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        userService.updateUser(user.getId(), userUpdateDto);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void getUserById() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        userService.getUserById(anyLong());
        verify(userRepository, times(1)).findById(anyLong());
    }

    @Test
    void deleteUserById() {
        userService.deleteUserById(anyLong());
        verify(userRepository, times(1)).deleteById(anyLong());
    }
}