package io.bearcave.yakba.rest;

import io.bearcave.yakba.AbstractIntegrationTest;
import io.bearcave.yakba.dao.BoardRepository;
import io.bearcave.yakba.dao.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

class BoardUserAccessEndpointTest extends AbstractIntegrationTest {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WebTestClient webClient;

    @WithMockUser(value = AbstractIntegrationTest.TESTER_ID)
    @Test
    void shouldAddUserFromBoard() {
        TODO();
    }

    @WithMockUser(value = AbstractIntegrationTest.TESTER_ID)
    @Test
    void shouldNotAddUserFromBoardWhenHeIsAlreadyMember() {
        TODO();

    }

    @WithMockUser(value = AbstractIntegrationTest.TESTER_ID)
    @Test
    void onlyAdminIsAllowedToAddUser() {
        TODO();
    }

    @WithMockUser(value = AbstractIntegrationTest.TESTER_ID)
    @Test
    void onlyAdminIsAllowedToRemoveUser() {
        TODO();
    }

    @WithMockUser(value = AbstractIntegrationTest.TESTER_ID)
    @Test
    void shouldRemoveUserFromBoard() {
        TODO();
    }
}