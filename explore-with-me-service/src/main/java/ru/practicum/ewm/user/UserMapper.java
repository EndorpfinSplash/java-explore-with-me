package ru.practicum.ewm.user;


import ru.practicum.ewm.user.dto.NewUserRequest;
import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.dto.UserUpdateDto;

public class UserMapper {

    public static UserDto toUserOutputDto(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }

    public static User toUser(NewUserRequest newUserRequest) {
        User user = User.builder().build();
        user.setEmail(newUserRequest.getEmail());
        user.setName(newUserRequest.getName());
        return user;
    }

    public static User toUser(User userForUpdate, UserUpdateDto userUpdateDto) {
        User user = userForUpdate.toBuilder().build();
        user.setId(userForUpdate.getId());
        String email = userUpdateDto.getEmail();
        if (email != null) {
            user.setEmail(email);
        }
        String name = userUpdateDto.getName();
        if (name != null) {
            user.setName(name);
        }
        return user;
    }

}
