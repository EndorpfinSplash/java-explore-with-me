package ru.practicum.ewm.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.ewm.user.dto.NewUserRequest;
import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.dto.UserUpdateDto;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private static final Long USER_ID = 1L;

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final UserDto userOutputDto = UserDto.builder()
            .id(USER_ID)
            .name("User Name")
            .email("TEST@EMAIL.com")
            .build();


    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void getAllUsers() throws Exception {
        when(userService.getAllUsers()).thenReturn(List.of(userOutputDto));

        mockMvc.perform(get("/admin/users/all")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void getUser() throws Exception {
        when(userService.getUserById(anyLong())).thenReturn(userOutputDto);

        mockMvc.perform(get("/admin/users/{id}", USER_ID)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userOutputDto.getId()), Long.class));
        verify(userService, times(1)).getUserById(USER_ID);
    }

    @Test
    void createUser() throws Exception {
        NewUserRequest newUserRequest = NewUserRequest.builder()
                .name("TEST_USERNAME")
                .email("TEST@EMAIL.com")
                .build();

        when(userService.createUser(any(NewUserRequest.class))).thenReturn(userOutputDto);

        mockMvc.perform(post("/admin/users")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(objectMapper.writeValueAsString(newUserRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(userOutputDto.getId()), Long.class));
        verify(userService, times(1)).createUser(newUserRequest);
    }

    @Test
    void updateUser() throws Exception {
        UserUpdateDto userUpdateDto = UserUpdateDto.builder()
                .name("Updated test name")
                .build();

        when(userService.updateUser(anyLong(), any(UserUpdateDto.class))).thenReturn(userOutputDto);

        mockMvc.perform(patch("/admin/users/{id}", USER_ID)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(objectMapper.writeValueAsString(userUpdateDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userOutputDto.getId()), Long.class));
        verify(userService, times(1)).updateUser(USER_ID, userUpdateDto);
    }

    @Test
    void deleteUser() throws Exception {
        mockMvc.perform(delete("/admin/users/{id}", USER_ID)
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent());
        verify(userService, times(1)).deleteUserById(USER_ID);
    }
}