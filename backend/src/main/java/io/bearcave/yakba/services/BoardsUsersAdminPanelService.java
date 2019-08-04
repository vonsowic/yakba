package io.bearcave.yakba.services;

import io.bearcave.yakba.dao.BoardRepository;
import io.bearcave.yakba.dao.UserRepository;
import io.bearcave.yakba.exceptions.Conflict;
import io.bearcave.yakba.exceptions.NotFound;
import io.bearcave.yakba.models.Board;
import io.bearcave.yakba.models.UserBoardAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;

@Service
public class BoardsUsersAdminPanelService {

    private final BoardRepository boardRepository;

    private final BoardService boardService;

    private final UserRepository userRepository;

    @Autowired
    public BoardsUsersAdminPanelService(BoardRepository boardRepository, BoardService boardService, UserRepository userRepository) {
        this.boardRepository = boardRepository;
        this.boardService = boardService;
        this.userRepository = userRepository;
    }

    public Mono<Void> addUserToBoardAsAdmin(String boardId, String userToBeAdded, String boardAdmin) {
        return userRepository.findById(userToBeAdded)
                .switchIfEmpty(Mono.error(() -> new NotFound(String.format("User %s does not exist", userToBeAdded))))
                .flatMap(__ -> boardService.getBoardForAdminWithoutDetails(boardId, boardAdmin))
                .flatMap(board -> {
                    if (isUserMemberOfBoard(userToBeAdded, board)) {
                        return Mono.error(Conflict::new);
                    }

                    return Mono.just(board);
                })
                .flatMap(board -> {
                    var updatedAccesses = new ArrayList<>(board.getAccesses());
                    updatedAccesses.add(new UserBoardAccess(userToBeAdded));
                    board.setAccesses(updatedAccesses);
                    return boardRepository.save(board);
                })
                .then();
    }

    private boolean isUserMemberOfBoard(String user, Board board) {
        return board.getAccesses()
                .stream()
                .map(UserBoardAccess::getUserId)
                .anyMatch(userId -> userId.equals(user));
    }
}
