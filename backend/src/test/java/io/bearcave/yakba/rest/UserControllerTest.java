package io.bearcave.yakba.rest;

import io.bearcave.yakba.AbstractIntegrationTest;
import io.bearcave.yakba.dao.UserRepository;
import io.bearcave.yakba.dto.CreateUserRQ;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.web.reactive.function.BodyInserters;

import static org.hamcrest.Matchers.*;

class UserControllerTest extends AbstractIntegrationTest {

    private final static String BASE_URL = "/api/user";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void shouldRegisterNewUserAndEncodePassword() {
        var request = new CreateUserRQ();
        request.setUsername("jamesbond");
        request.setEmail("james.bond@mi6.uk");
        request.setPassword("007");

        webClient.post()
                .uri(BASE_URL)
                .body(BodyInserters.fromObject(request))
                .exchange()
                .expectStatus().isCreated();

        var createdUser = userRepository.findFirstByUsername(request.getUsername()).block();
        Assert.assertThat(createdUser, is(not(nullValue())));
        Assert.assertTrue(passwordEncoder.matches(request.getPassword(), createdUser.getPassword()));
    }

    @Test
    public void shouldNotRegisterNewUserWhenUsernameIsEmpty() {
        var request = new CreateUserRQ();
        request.setEmail("james.bond@mi6.uk");
        request.setPassword("007");

        webClient.post()
                .uri(BASE_URL)
                .body(BodyInserters.fromObject(request))
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    public void shouldNotRegisterNewUserWhenEmailIsEmpty() {
        var request = new CreateUserRQ();
        request.setUsername("jamesbond");
        request.setPassword("007");

        webClient.post()
                .uri(BASE_URL)
                .body(BodyInserters.fromObject(request))
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    public void shouldNotRegisterNewUserWhenPasswordIsEmpty() {
        var request = new CreateUserRQ();
        request.setUsername("jamesbond");
        request.setEmail("james.bond@mi6.uk");

        webClient.post()
                .uri(BASE_URL)
                .body(BodyInserters.fromObject(request))
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @WithMockUser(value = AbstractIntegrationTest.TESTER_ID)
    public void shouldReturnUsernameByUserId() {
        webClient.get()
                .uri(BASE_URL + "/" + TESTER_ID)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.username").isEqualTo(getTester().getUsername())
                .jsonPath("$.email").isEmpty();
    }

    @Test
    @WithMockUser(value = AbstractIntegrationTest.TESTER_ID)
    public void shouldReturnNotFoundStatusWhenUserWithGivenIdDoesNotExist() {
        webClient.get()
                .uri(BASE_URL + "/SOME_USER_ID")
                .exchange()
                .expectStatus().isNotFound();
    }

}