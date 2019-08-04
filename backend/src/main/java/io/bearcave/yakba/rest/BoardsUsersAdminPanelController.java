package io.bearcave.yakba.rest;

import io.bearcave.yakba.services.BoardsUsersAdminPanelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.security.Principal;

@RestController
@RequestMapping("/api/board/{boardId}/admin/user")
public class BoardsUsersAdminPanelController {

    private final BoardsUsersAdminPanelService boardsUsersAdminPanelService;

    @Autowired
    public BoardsUsersAdminPanelController(BoardsUsersAdminPanelService boardsUsersAdminPanelService) {
        this.boardsUsersAdminPanelService = boardsUsersAdminPanelService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{username}")
    public Mono<Void> addUserToBoardAsAdmin(
            @PathVariable String boardId,
            @PathVariable("username") String userToBeAdded,
            Principal user
    ) {
        return boardsUsersAdminPanelService.addUserToBoardAsAdmin(boardId, userToBeAdded, user.getName());
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{username}")
    public Mono<Void> removeUserFromBoard(
            @PathVariable String boardId,
            @PathVariable("username") String userToBeRemoved,
            Principal user
    ) {
        return boardsUsersAdminPanelService.removeUserFromBoardAsUser(userToBeRemoved, boardId, user.getName());
    }

}
