package io.bearcave.yakba.dao;

import io.bearcave.yakba.models.UserBoardAccess;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface BoardAccessRepository extends ReactiveMongoRepository<UserBoardAccess, String> {

    Flux<UserBoardAccess> findAllByUserId(String userId);

    Mono<UserBoardAccess> findFirstByUserIdAndBoardId(String userId, String boardId);
}
