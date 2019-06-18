package io.bearcave.yakba.rest;

import io.bearcave.yakba.dao.UserRepository;
import io.bearcave.yakba.dto.CreateUserRQ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Mono<Void> registerNewUser(@RequestBody CreateUserRQ createUserRQ) {
        return Mono.empty();
    }


}
