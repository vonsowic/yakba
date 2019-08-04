package io.bearcave.yakba.rest;

import io.bearcave.yakba.AbstractBoardIntegrationTest;
import io.bearcave.yakba.models.UserBoardAccess;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;

import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.assertThat;

class BoardsUsersAdminPanelControllerTest extends AbstractBoardIntegrationTest {

    @Test
    @WithMockUser(TESTER_ID)
    void newUserShouldBeAddedByAdmin() {
        var newUser = createUser("NewUserToBeAddedToBoard").block();

        webClient.post()
                .uri(String.format("/api/board/%s/admin/user/%s", board.getId(), newUser.getUsername()))
                .exchange()
                .expectStatus().isCreated()
                .expectBody().isEmpty();

        var updatedBoard = boardRepository.findByIdWithoutCardDetails(board.getId()).block();
        assertThat(updatedBoard.getAccesses(), hasItem(new UserBoardAccess(newUser.getUsername())));
    }

    @Test
    @WithMockUser(TESTER2_ID)
    void butNormalUserIsNotAllowedToDoThat() {
        var newUser = createUser("NewUserToBeAddedToBoard").block();

        webClient.post()
                .uri(String.format("/api/board/%s/admin/user/%s", board.getId(), newUser.getUsername()))
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    @WithMockUser(TESTER_ID)
    void shouldReturnNotFoundWhenAddingNonExistingUser() {
        webClient.post()
                .uri(String.format("/api/board/%s/admin/user/XXX", board.getId()))
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @WithMockUser(TESTER_ID)
    void shouldReturnConflictWhenAddingUserWhoIsAlreadyMemberOfBoard() {
        webClient.post()
                .uri(String.format("/api/board/%s/admin/user/%s", board.getId(), TESTER2_ID))
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.CONFLICT);
    }

}