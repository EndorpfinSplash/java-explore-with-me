package ru.practicum.ewm.user;


import ru.practicum.ewm.user.dto.UserCreationDTO;
import ru.practicum.ewm.user.dto.UserOutputDto;
import ru.practicum.ewm.user.dto.UserUpdateDto;

public class UserMapper {

    public static UserOutputDto toUserOutputDto(User user) {
        return new UserOutputDto(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }

    public static User toUser(UserCreationDTO userCreationDTO) {
        User user = User.builder().build();
        user.setEmail(userCreationDTO.getEmail());
        user.setName(userCreationDTO.getName());
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
