package io.bearcave.yakba.security;

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import reactor.core.publisher.Mono;


public class YakbaAuthenticationManager implements ReactiveAuthenticationManager {
    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        if (authentication.isAuthenticated()) {
            return Mono.just(authentication);
        }

        return null;
    }
}
