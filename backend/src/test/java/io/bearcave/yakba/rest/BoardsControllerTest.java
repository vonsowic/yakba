package io.bearcave.yakba.rest;

import io.bearcave.yakba.AbstractIntegrationTest;
import io.bearcave.yakba.dao.BoardRepository;
import io.bearcave.yakba.models.Board;
import net.minidev.json.JSONArray;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.BodyInserters;

import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

class BoardsControllerTest extends AbstractIntegrationTest {

    @Autowired
    private BoardRepository boardRepository;

    @Test
    void shouldReturnOneBoardById() {
        var board = new Board("XYZ", TESTER_ID);
        boardRepository.insert(board)
                .block();

        webClient.get()
                .uri("/api/board/" + board.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$").isEqualTo(board);
    }

    @Test
    void shouldReturnNotFoundStatusIfBoardDoesNotExist() {
        webClient.get()
                .uri("/api/board/someBoardId")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void shouldReturnForbiddenStatusIfUserIsNotAMemberOfBoard() {
        var board = new Board();
        boardRepository.insert(board)
                .block();

        webClient.get()
                .uri("/api/board/" + board.getId())
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    void returnsBoardForWhichUserHasAccessWithoutColumns() {
        var board = new Board("XYZ", TESTER_ID);
        boardRepository.insert(board)
                .block();

        var someBoard = new Board();
        someBoard.setName("zzzzzzzzz");
        boardRepository.insert(someBoard)
                .block();

        webClient.get()
                .uri("/api/board")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].name").isEqualTo(board.getName())
                .jsonPath("$[0].columns").doesNotHaveJsonPath()
                .jsonPath("$").value(json -> Assert.assertEquals(1, ((JSONArray) json).size()));
    }

    @Test
    void shouldCreateNewBoardAndAddAdminAccessToUser() {
        var boardName = "asdnfsaodnf";
        var boardReq = Collections.singletonMap("name", boardName);
        webClient.post()
                .uri("/api/board")
                .body(BodyInserters.fromObject(boardReq))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("id").isNotEmpty();
    }

    @Test
    void shouldNotCreateNewBoardWithoutName() {
        var boardReq = Collections.singletonMap("name", "");
        webClient.post()
                .uri("/api/board")
                .body(BodyInserters.fromObject(boardReq))
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void shouldRemoveBoard() {
        var board = new Board("XYZ", TESTER_ID);
        boardRepository.insert(board)
                .block();

        webClient.delete()
                .uri("/api/board/" + board.getId())
                .exchange()
                .expectStatus().isNoContent();

        var deletedBoard = boardRepository.findById(board.getId()).block();
        Assert.assertThat(deletedBoard, is(nullValue()));
    }

    @Test
    void boardCanBeRemovedJustByAdmin() {
        var board = new Board("XYZ", "SOME OTHER USER");
        boardRepository.insert(board)
                .block();

        webClient.delete()
                .uri("/api/board/" + board.getId())
                .exchange()
                .expectStatus().isForbidden();
    }

}