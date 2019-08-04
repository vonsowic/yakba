package io.bearcave.yakba.services;

import io.bearcave.yakba.dao.UserRepository;
import io.bearcave.yakba.dto.CreateUserRQ;
import io.bearcave.yakba.exceptions.Conflict;
import io.bearcave.yakba.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Mono<User> registerNewUser(CreateUserRQ createUserRQ) {
        var encodedPassword = passwordEncoder.encode(createUserRQ.getPassword());

        var user = new User();
        user.setUsername(createUserRQ.getUsername());
        user.setEmail(createUserRQ.getEmail());
        user.setPassword(encodedPassword);

        return userRepository.insert(user)
                .doOnError(DuplicateKeyException.class, e -> {
                    throw new Conflict("Username or email already exist");
                });
    }
}
