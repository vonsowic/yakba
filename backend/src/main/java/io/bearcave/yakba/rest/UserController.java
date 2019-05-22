package io.bearcave.yakba.rest;

import io.bearcave.yakba.dao.UserRepository;
import io.bearcave.yakba.dto.AuthDTO;
import io.bearcave.yakba.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/registration")
    public Mono<ResponseEntity> registerNewUser(@RequestBody AuthDTO auth) {
        var newUser = new User();
        newUser.setUsername(auth.getUsername());
        newUser.setPassword(BCrypt.hashpw(auth.getPassword(), BCrypt.gensalt()));
        return this.userRepository.save(newUser)
                .map(user -> ResponseEntity.created(URI.create("/api/user/login")).build())
                .onErrorReturn(new ResponseEntity(HttpStatus.CONFLICT));
    }
}
