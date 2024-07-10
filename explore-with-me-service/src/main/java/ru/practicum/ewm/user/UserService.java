package ru.practicum.ewm.user;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.user.dto.UserCreationDTO;
import ru.practicum.ewm.user.dto.UserOutputDto;
import ru.practicum.ewm.user.dto.UserUpdateDto;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {


    private final UserRepository userStorage;

    public Collection<UserOutputDto> getAllUsers() {
        return userStorage.findAll().stream()
                .map(UserMapper::toUserOutputDto)
                .collect(Collectors.toList());
    }

    public UserOutputDto createUser(UserCreationDTO userCreationDTO) {
        User user = UserMapper.toUser(userCreationDTO);
        User savedUser = null;
//        try {
            savedUser = userStorage.save(user);
//        } catch (DataIntegrityViolationException e) {
//            throw new NonUniqueEmail(String.format(e.getMessage()));
//            throw new NonUniqueEmail(String.format("Email = %s address has already in use", user.getEmail()));
//        }
        return UserMapper.toUserOutputDto(savedUser);
    }

    public UserOutputDto updateUser(Integer userId, UserUpdateDto userUpdateDto) {
        User userForUpdate = userStorage.findById(userId).orElseThrow(
//                () -> new UserNotFoundException(MessageFormat.format("User with userId={0} not found", userId))
        );
        User editedUser = UserMapper.toUser(userForUpdate, userUpdateDto);

        User updatedUser = null;
        try {
            updatedUser = userStorage.save(editedUser);
        } catch (DataIntegrityViolationException e) {

//            throw new NonUniqueEmail(String.format("Email = %s address has already in use", updatedUser.getEmail()));
        }

        return UserMapper.toUserOutputDto(updatedUser);
    }

    public UserOutputDto getUserById(Integer userId) {
        User user = userStorage.findById(userId)
                .orElseThrow(
//                        () -> new UserNotFoundException(MessageFormat.format("User with userId={0} not found", userId))
                );
        return UserMapper.toUserOutputDto(user);
    }

    public void deleteUserById(Integer id) {
        userStorage.deleteById(id);
    }

    public Collection<UserOutputDto> getUsers(List<Integer> ids, Integer from, Integer size) {
        Pageable page = PageRequest.of(from > 0 ? from / size : 0, size);
        return userStorage.findAllByIdOrderById(ids, page).stream()
                .map(UserMapper::toUserOutputDto)
                .collect(Collectors.toList());
    }
}
