package io.bearcave.yakba.dao;

import io.bearcave.yakba.models.User;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveMongoRepository<User, String> {

    Mono<User> findFirstByUsername(String username);

    @Query(value = "{_id: ?0}", fields = "{email: 0}")
    Mono<User> findUsernameById(String userId);
}
