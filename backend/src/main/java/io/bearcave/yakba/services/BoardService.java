package io.bearcave.yakba.services;

import io.bearcave.yakba.dao.BoardAccessRepository;
import io.bearcave.yakba.dao.BoardRepository;
import io.bearcave.yakba.dto.UserBoardAccessDTO;
import io.bearcave.yakba.models.Board;
import io.bearcave.yakba.models.UserBoardAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.function.Function;

@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardAccessRepository boardAccessRepository;

    @Autowired
    public BoardService(BoardRepository boardRepository, BoardAccessRepository boardAccessRepository) {
        this.boardRepository = boardRepository;
        this.boardAccessRepository = boardAccessRepository;
    }

    public Flux<Board> getBoardsForUser(String userId) {
        return Flux.mergeSequential(boardAccessRepository.findAllByUserId(userId)
                .map(userBoardAccess -> boardRepository.findById(userBoardAccess.getBoardId())));
    }

    public Mono<Board> createNewBoard(Board board, String userId) {
        return boardRepository.insert(board);
    }
}
