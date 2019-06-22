package io.bearcave.yakba.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.util.Collections;
import java.util.Map;

@RestController
public class AuthController {

    @RequestMapping(value = "/api/login", method = {RequestMethod.GET, RequestMethod.POST})
    public Mono<Map<String, String>> me(Mono<Principal> user) {
        return user
                .map(Principal::getName)
                .map(username -> Collections.singletonMap("username", username));
    }
}
