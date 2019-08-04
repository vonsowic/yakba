package io.bearcave.yakba.rest;

import io.bearcave.yakba.dto.CreateUserRQ;
import io.bearcave.yakba.exceptions.BadRequest;
import io.bearcave.yakba.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Mono<Map<String, String>> me(Mono<Principal> user) {
        return user
                .map(Principal::getName)
                .map(username -> Collections.singletonMap("username", username));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Mono<Void> registerNewUser(@RequestBody CreateUserRQ createUserRQ) {
        if (!createUserRQ.isNotEmpty()) {
            throw new BadRequest();
        }

        return userService.registerNewUser(createUserRQ)
                .then();
    }
}
