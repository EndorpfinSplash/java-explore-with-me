package ru.practicum.ewm.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.exception.EntityNotFoundException;
import ru.practicum.ewm.user.dto.NewUserRequest;
import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.dto.UserUpdateDto;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userStorage;

    public Collection<UserDto> getAllUsers() {
        return userStorage.findAll().stream()
                .map(UserMapper::toUserOutputDto)
                .collect(Collectors.toList());
    }

    public UserDto createUser(NewUserRequest newUserRequest) {
        User user = UserMapper.toUser(newUserRequest);
        User savedUser = userStorage.save(user);
        return UserMapper.toUserOutputDto(savedUser);
    }

    public UserDto updateUser(Long userId, UserUpdateDto userUpdateDto) {
        User userForUpdate = findUser(userId);
        User editedUser = UserMapper.toUser(userForUpdate, userUpdateDto);
        User updatedUser = userStorage.save(editedUser);
        return UserMapper.toUserOutputDto(updatedUser);
    }

    public UserDto getUserById(Long userId) {
        User user = findUser(userId);
        return UserMapper.toUserOutputDto(user);
    }

    public User findUser(Long userId) {
        return userStorage.findById(userId)
                .orElseThrow(
                        () -> new EntityNotFoundException(MessageFormat.format("User with userId={0} was not found", userId))
                );
    }

    public void deleteUserById(Long id) {
        userStorage.deleteById(id);
    }

    public Collection<UserDto> getUsers(List<Long> ids, Integer from, Integer size) {
        Pageable page = PageRequest.of(from > 0 ? from / size : 0, size);
        if (ids == null || ids.isEmpty()) {
            return userStorage.findAll(page).stream()
                    .map(UserMapper::toUserOutputDto)
                    .collect(Collectors.toList());
        }
        return userStorage.findAllByIdOrderById(ids, page).stream()
                .map(UserMapper::toUserOutputDto)
                .collect(Collectors.toList());
    }
}
