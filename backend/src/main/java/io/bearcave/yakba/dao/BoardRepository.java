package io.bearcave.yakba.dao;

import io.bearcave.yakba.models.Board;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface BoardRepository extends ReactiveMongoRepository<Board, String> {

    @Query(value = "{}", fields = "{columns: 0}")
    Flux<Board> findAllOverview();
}
