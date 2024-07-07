package ru.practicum.ewm.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.user.dto.UserCreationDTO;
import ru.practicum.ewm.user.dto.UserOutputDto;
import ru.practicum.ewm.user.dto.UserUpdateDto;


import javax.validation.Valid;
import java.util.Collection;


@RestController
@RequestMapping(path = "/admin/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @GetMapping
    public Collection<UserOutputDto> getAllUsers() {
        log.info("GET request to fetch collection of users received.");
        return userService.getAllUsers();
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
