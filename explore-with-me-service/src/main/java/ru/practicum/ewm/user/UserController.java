package ru.practicum.ewm.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.user.dto.UserCreationDTO;
import ru.practicum.ewm.user.dto.UserOutputDto;
import ru.practicum.ewm.user.dto.UserUpdateDto;


import javax.validation.Valid;
import java.util.Collection;
import java.util.List;


@RestController
@RequestMapping(path = "/admin/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {
    private final UserService userService;

    @GetMapping("/all")
    public Collection<UserOutputDto> getAllUsers() {
        log.info("GET request to fetch collection of all users received.");
        return userService.getAllUsers();
    }

    @GetMapping
    public Collection<UserOutputDto> getUsers(
            @RequestParam List<Integer> ids,
            @RequestParam(value = "from", defaultValue = "0") Integer from,
            @RequestParam(value = "size", defaultValue = "10") Integer size
    ) {
        log.info("GET request to fetch collection of users {} from = {} with size = {} received.", ids, from, size);
        return userService.getUsers(ids,  from,  size);
    }

    @GetMapping("/{id}")
    public UserOutputDto getUser(@PathVariable("id") Integer id) {
        log.info("GET request to fetch user_id={} received.", id);
        return userService.getUserById(id);
    }


    @PostMapping
    public UserOutputDto createUser(@Valid @RequestBody UserCreationDTO userCreationDTO) {
        log.info("POST request to create {} received.", userCreationDTO);
        return userService.createUser(userCreationDTO);
    }

    @PatchMapping("/{id}")
    public UserOutputDto updateUser(@PathVariable("id") Integer id, @Valid @RequestBody UserUpdateDto userUpdateDto) {
        log.info("PATCH request to update user_id={} received.", id);
        return userService.updateUser(id, userUpdateDto);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable("id") Integer id) {
        log.info("Delete request to remove user_id={} received.", id);
        userService.deleteUserById(id);
    }

}
