package ru.practicum.ewm.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.user.dto.NewUserRequest;
import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.dto.UserUpdateDto;

import jakarta.validation.Valid;
import java.util.Collection;
import java.util.List;


@RestController
@RequestMapping(path = "/admin/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @GetMapping("/all")
    public Collection<UserDto> getAllUsers() {
        log.info("GET request to fetch collection of all users received.");
        Collection<UserDto> allUsers = userService.getAllUsers();
        log.info("{} users found.", allUsers.size());
        return allUsers;
    }

    @GetMapping
    public Collection<UserDto> getUsers(
            @RequestParam (required = false) List<Long> ids,
            @RequestParam(value = "from", defaultValue = "0") Integer from,
            @RequestParam(value = "size", defaultValue = "10") Integer size
    ) {
        log.info("GET request to fetch collection of users {} from = {} with size = {} received.", ids, from, size);
        Collection<UserDto> users = userService.getUsers(ids, from, size);
        log.info("{} users found", users.size());
        return users;

    }

    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable("id") Long id) {
        log.info("GET request to fetch user_id={} received.", id);
        UserDto userById = userService.getUserById(id);
        log.info("{} user found.", userById);
        return userById;
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@RequestBody @Valid final NewUserRequest newUserRequest) {
        log.info("POST request to create {} received.", newUserRequest);
        UserDto user = userService.createUser(newUserRequest);
        log.info("{} user created.", user);
        return user;
    }

    @PatchMapping("/{id}")
    public UserDto updateUser(@PathVariable("id") Long id, @RequestBody final UserUpdateDto userUpdateDto) {
        log.info("PATCH request to update user_id={} received.", id);
        UserDto userDto = userService.updateUser(id, userUpdateDto);
        log.info("{} user updated.", userDto);
        return userDto;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable("id") final Long id) {
        log.info("Delete request to remove user_id={} received.", id);
        userService.deleteUserById(id);
    }

}
