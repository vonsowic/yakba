package io.bearcave.yakba.rest;

import io.bearcave.yakba.AbstractIntegrationTest;
import io.bearcave.yakba.dao.BoardRepository;
import io.bearcave.yakba.dao.UserRepository;
import net.minidev.json.JSONArray;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

class UserEndpointTest extends AbstractIntegrationTest {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WebTestClient webClient;

    @Test
    void shouldCreateNewUser() {
        TODO();
    }

    @Test
    void shouldReturn400StatusWhenUsernameOrPasswordIsMissing() {
        TODO();
    }

    @Test
    void shouldReturnConflictStatusWhenUsernameAlreadyExists() {
        TODO();
    }

    @WithMockUser(value = AbstractIntegrationTest.TESTER_ID)
    @Test
    void shouldFindUserByUsernamePassedInQueryParam() {
        TODO();
    }

    @WithMockUser(value = AbstractIntegrationTest.TESTER_ID)
    @Test
    void shouldReturnEmptyListWhenSearchQueryParamIsEmpty() {
        webClient.get()
                .uri("/api/user")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$").value(json -> Assert.assertEquals(0, ((JSONArray) json).size()));
    }

}