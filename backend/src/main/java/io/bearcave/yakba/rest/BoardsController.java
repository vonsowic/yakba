package io.bearcave.yakba.rest;

import io.bearcave.yakba.models.Board;
import io.bearcave.yakba.services.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.security.Principal;

@RestController
@RequestMapping("/api/board")
public class BoardsController {

    private final BoardService boardService;

    @Autowired
    public BoardsController(BoardService boardService) {
        this.boardService = boardService;
    }

    @GetMapping
    public Flux<Board> getUsersBoards(Principal user) {
        return boardService.getBoardsForUser(user.getName());
    }

    @PostMapping
    public void createNewBoard(@RequestBody Board board) {

    }
}
