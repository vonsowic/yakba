package io.bearcave.yakba.rest;

import io.bearcave.yakba.AbstractIntegrationTest;
import io.bearcave.yakba.dao.BoardAccessRepository;
import io.bearcave.yakba.dao.BoardRepository;
import io.bearcave.yakba.models.Board;
import io.bearcave.yakba.models.UserBoardAccess;
import net.minidev.json.JSONArray;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

class BoardsEndpointTest extends AbstractIntegrationTest {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private BoardAccessRepository boardAccessRepository;

    @Autowired
    private WebTestClient webClient;

    @WithMockUser(value = AbstractIntegrationTest.TESTER_ID)
    @Test
    void returnsBoardForWhichUserHasAccess() throws Exception {
        var board = new Board();
        board.setName("XYZ");
        boardRepository.insert(board)
                .block();

        var someBoard = new Board();
        someBoard.setName("zzzzzzzzz");
        boardRepository.insert(someBoard)
                .block();

        boardAccessRepository.insert(new UserBoardAccess(getTester().getId(), board.getId()))
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

    }

    @Test
    void shouldAddUserAccess() {

    }

    @Test
    void shouldRemoveUserFromBoard() {

    }

    @Test
    void shouldRemoveBoardByAdmin() {

    }

    @Test
    void shouldNotRemoveBoardByUserWithNoAdminAccess() {

    }

}