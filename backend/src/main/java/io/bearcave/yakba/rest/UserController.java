package io.bearcave.yakba.rest;

import io.bearcave.yakba.dto.CreateUserRQ;
import io.bearcave.yakba.exceptions.BadRequest;
import io.bearcave.yakba.models.User;
import io.bearcave.yakba.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Mono<Void> registerNewUser(@RequestBody CreateUserRQ createUserRQ) {
        if (!createUserRQ.isNotEmpty()) {
            throw new BadRequest();
        }

        return userService.registerNewUser(createUserRQ);
    }

    @GetMapping("/{userId}")
    public Mono<User> findOneUserById(@PathVariable String userId) {
        return userService.findUsernameById(userId);
    }
}
