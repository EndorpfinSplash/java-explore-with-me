package ru.practicum.ewm.user;

import org.junit.jupiter.api.Test;
import ru.practicum.ewm.user.dto.NewUserRequest;
import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.dto.UserUpdateDto;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UserMapperTest {
    private final String testUserName = "Test User";
    private final String testUserEmail = "TEST@EMAIL.com";

    @Test
    void toUserOutputDto() {

        User testUser = User.builder()
                .id(1L)
                .name(testUserName)
                .email(testUserEmail)
                .build();
        UserDto resultUserOutputDto = UserMapper.toUserOutputDto(testUser);
        assertNotNull(resultUserOutputDto);
        assertEquals(testUser.getId(), resultUserOutputDto.getId());
        assertEquals(testUser.getName(), resultUserOutputDto.getName());
        assertEquals(testUser.getEmail(), resultUserOutputDto.getEmail());

    }

    @Test
    void toUser() {
        NewUserRequest newUserRequest = NewUserRequest.builder()
                .name(testUserName)
                .email(testUserEmail)
                .build();
        User resultUser = UserMapper.toUser(newUserRequest);
        assertNotNull(resultUser);
        assertEquals(testUserName, resultUser.getName());
        assertEquals(testUserEmail, resultUser.getEmail());
    }

    @Test
    void testToUser() {
        User userForUpdate = User.builder()
                .id(7L)
                .name("origin name")
                .email("origin@email.com")
                .build();
        UserUpdateDto userUpdateDto = UserUpdateDto.builder()
                .name(testUserName)
                .email(testUserEmail)
                .build();
        User resultUser = UserMapper.toUser(userForUpdate, userUpdateDto);

        assertNotNull(resultUser);
        assertEquals(testUserName, resultUser.getName());
        assertEquals(testUserEmail, resultUser.getEmail());
        assertEquals(userForUpdate.getId(), resultUser.getId());
    }
}