package io.bearcave.yakba.services;

import io.bearcave.yakba.dao.BoardRepository;
import io.bearcave.yakba.exceptions.Forbidden;
import io.bearcave.yakba.models.Board;
import io.bearcave.yakba.models.BoardAccessLevel;
import io.bearcave.yakba.models.UserBoardAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Service
public class BoardService {

    private final BoardRepository boardRepository;

    @Autowired
    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    public Flux<Board> getBoardsForUser(String userId) {
        return boardRepository.findAll()
                .filter(board -> board.getAccesses()
                        .stream()
                        .map(UserBoardAccess::getUserId)
                        .anyMatch(accessUserId -> Objects.equals(accessUserId, userId)));  // FIXME: optimize
    }

    public Mono<Board> createNewBoard(String boardName, String userId) {
        return boardRepository.insert(new Board(boardName, userId));
    }

    public Mono<Void> deleteBoard(String boardId, String ownerId) {
        return boardRepository.findById(boardId)
                .map(board -> {
                    if (!hasUserAdminAccess(board, ownerId)) {
                        throw new Forbidden();
                    }

                    return Mono.empty();
                })
                .then();
    }

    private boolean hasUserAdminAccess(Board board, String userId) {
        return board.getAccesses()
                .stream()
                .filter(access -> Objects.equals(access.getAccessLevel(), BoardAccessLevel.ADMIN))
                .anyMatch(access -> Objects.equals(access.getUserId(), userId));
    }
}
