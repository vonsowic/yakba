package io.bearcave.yakba.services;

import io.bearcave.yakba.dao.BoardRepository;
import io.bearcave.yakba.exceptions.Forbidden;
import io.bearcave.yakba.exceptions.NotFound;
import io.bearcave.yakba.models.Board;
import io.bearcave.yakba.models.BoardAccessLevel;
import io.bearcave.yakba.models.UserBoardAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

@Service
public class BoardService {

    private final BoardRepository boardRepository;

    @Autowired
    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    public Mono<Board> getBoardForUser(String boardId, String username) {
        return getBoardForUser(boardId, username, boardRepository::findById);
    }

    public Mono<Board> getBoardForUserWithoutDetails(String boardId, String userId) {
        return getBoardForUser(boardId, userId, boardRepository::findByIdWithoutCardDetails);
    }

    private Mono<Board> getBoardForUser(String boardId, String username, Function<String, Mono<Board>> findBoardById) {
        return findBoardById.apply(boardId)
                .switchIfEmpty(Mono.error(() -> new NotFound(String.format("Board %s does not exist", boardId))))
                .map(board -> {
                    if (!hasUserAccess(board, username)) {
                        throw new Forbidden(String.format(
                                "User %s does not have access to %s[%s]", username, board.getName(), board.getId()
                        ));
                    }

                    return board;
                });
    }

    public Mono<List<UserBoardAccess>> getUsersOfBoard(String boardId) {
        return boardRepository.findByIdWithoutCardDetails(boardId)
                .switchIfEmpty(Mono.error(() -> new NotFound(String.format("Board %s does not exist", boardId))))
                .map(Board::getAccesses);
    }

    public Mono<Board> getBoardForAdminWithoutDetails(String boardId, String username) {
        return boardRepository.findByIdWithoutCardDetails(boardId)
                .switchIfEmpty(Mono.error(() -> new NotFound(String.format("Board %s does not exist", boardId))))
                .map(board -> {
                    if (!hasUserAdminAccess(board, username)) {
                        throw new Forbidden(String.format(
                                "User %s does not have access to %s[%s]", username, board.getName(), board.getId()
                        ));
                    }

                    return board;
                });
    }

    private boolean hasUserAccess(Board board, String username) {
        return board.getAccesses()
                .stream()
                .map(UserBoardAccess::getUserId)
                .anyMatch(id -> Objects.equals(id, username));
    }

    public Flux<Board> getBoardsForUser(String userId) {
        return boardRepository.findAllByUsernameWithoutColumns(userId);
    }

    private boolean hasUserAdminAccess(Board board, String userId) {
        return board.getAccesses()
                .stream()
                .filter(access -> Objects.equals(access.getAccessLevel(), BoardAccessLevel.ADMIN))
                .anyMatch(access -> Objects.equals(access.getUserId(), userId));
    }

    public Mono<Board> createNewBoard(String boardName, String username) {
        return boardRepository.insert(new Board(boardName, username));
    }

    public Mono<Void> deleteBoard(String boardId, String ownerId) {
        return boardRepository.findById(boardId)
                .flatMap(board -> {
                    if (!hasUserAdminAccess(board, ownerId)) {
                        throw new Forbidden();
                    }

                    return boardRepository.delete(board);
                })
                .then();
    }
}
