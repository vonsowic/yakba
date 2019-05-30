package io.bearcave.yakba.rest;

import io.bearcave.yakba.AbstractIntegrationTest;
import io.bearcave.yakba.dao.BoardRepository;
import io.bearcave.yakba.models.Board;
import net.minidev.json.JSONArray;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

class BoardsControllerTest extends AbstractIntegrationTest {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private WebTestClient webClient;

    @WithMockUser(value = AbstractIntegrationTest.TESTER_ID)
    @Test
    void returnsBoardForWhichUserHasAccess() throws Exception {
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
                    .jsonPath("$[0]").isEqualTo(board)
                    .jsonPath("$").value(json -> Assert.assertEquals(1, ((JSONArray) json).size()));
    }

    @WithMockUser(value = AbstractIntegrationTest.TESTER_ID)
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

    @WithMockUser(value = AbstractIntegrationTest.TESTER_ID)
    @Test
    void shouldNotCreateNewBoardWithoutName() {
        var boardReq = Collections.singletonMap("name", "");
        webClient.post()
                .uri("/api/board")
                .body(BodyInserters.fromObject(boardReq))
                .exchange()
                .expectStatus().isBadRequest();
    }

    @WithMockUser(value = AbstractIntegrationTest.TESTER_ID)
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

    @WithMockUser(value = AbstractIntegrationTest.TESTER_ID)
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