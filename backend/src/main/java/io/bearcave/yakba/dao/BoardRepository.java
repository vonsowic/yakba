package io.bearcave.yakba.dao;

import io.bearcave.yakba.models.Board;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface BoardRepository extends ReactiveMongoRepository<Board, String> {

    @Query(value = "{\"accesses.userId\": { $eq: ?0 }}}", fields = "{columns: 0}")
    Flux<Board> findAllByUsernameWithoutColumns(String username);

    @Query(value = "{_id: ?0}", fields = "{\"columns.cards.content\": 0, \"columns.cards.createdByUserId\": 0}")
    Mono<Board> findByIdWithoutCardDetails(String boardId);
}
