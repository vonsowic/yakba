package io.bearcave.yakba.dao;

import io.bearcave.yakba.models.Board;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends ReactiveMongoRepository<Board, String> {
}
