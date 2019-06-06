package io.bearcave.yakba.rest;

import io.bearcave.yakba.exceptions.BadRequest;
import io.bearcave.yakba.models.Board;
import io.bearcave.yakba.services.BoardService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/board")
public class BoardsController {

    private final BoardService boardService;

    @Autowired
    public BoardsController(BoardService boardService) {
        this.boardService = boardService;
    }

    @GetMapping("/{boardId}")
    public Mono<Board> getUsersBoards(@PathVariable String boardId, Principal user) {
        return boardService.getBoardForUser(boardId, user.getName());
    }

    @GetMapping
    public Flux<Board> getUsersBoards(Principal user) {
        return boardService.getBoardsForUser(user.getName());
    }

    @PostMapping
    public Mono<Board> createNewBoard(
            @RequestBody Map<String, String> body,
            Principal user) {
        var boardName = body.get("name");
        if (StringUtils.isEmpty(boardName)) {
            throw new BadRequest();
        }

        return boardService.createNewBoard(body.get("name"), user.getName());
    }

    @DeleteMapping(value = "/{boardId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public Mono<Void> deleteBoardByOwner(@PathVariable("boardId") String boardId, Principal user) {
        return boardService.deleteBoard(boardId, user.getName());
    }
}
