package io.bearcave.yakba.services;

import io.bearcave.yakba.dao.UserRepository;
import io.bearcave.yakba.security.YakbaUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class YakbaUserDetailsService implements ReactiveUserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public YakbaUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return userRepository.findById(username)
                .map(YakbaUserDetails::from)
                .switchIfEmpty(createUserNotFoundException());
    }

    private Mono<UserDetails> createUserNotFoundException() {
        return Mono.error(new UsernameNotFoundException(""));
    }
}
