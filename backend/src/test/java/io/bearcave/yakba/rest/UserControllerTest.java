package io.bearcave.yakba.rest;

import io.bearcave.yakba.AbstractIntegrationTest;
import io.bearcave.yakba.dao.UserRepository;
import io.bearcave.yakba.dto.CreateUserRQ;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.BodyInserters;

import static org.hamcrest.Matchers.*;

class UserControllerTest extends AbstractIntegrationTest {

    private final static String BASE_URL = "/api/user";

    @Autowired
    private UserRepository userRepository;

    @Test
    public void shouldRegisterNewUser() {
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
    }

    @Test
    public void shouldNotRegisterNewUserWhenUsernameIsDuplicated() {
        var request = new CreateUserRQ();
        request.setUsername("jamesbond");
        request.setEmail("james.bond@mi6.uk");
        request.setPassword("007");

        webClient.post()
                .uri(BASE_URL)
                .body(BodyInserters.fromObject(request))
                .exchange()
                .expectStatus().isCreated();

        var request2 = new CreateUserRQ();
        request2.setUsername("jamesbond");
        request2.setEmail("james.bond.007@mi6.uk");
        request2.setPassword("007");

        webClient.post()
                .uri(BASE_URL)
                .body(BodyInserters.fromObject(request))
                .exchange()
                .expectStatus().isEqualTo(409);
    }

    @Test
    public void shouldNotRegisterNewUserWhenEmailIsDuplicated() {
        var request = new CreateUserRQ();
        request.setUsername("jamesbond");
        request.setEmail("james.bond@mi6.uk");
        request.setPassword("007");

        webClient.post()
                .uri(BASE_URL)
                .body(BodyInserters.fromObject(request))
                .exchange()
                .expectStatus().isCreated();

        var request2 = new CreateUserRQ();
        request2.setUsername("james.bond");
        request2.setEmail("james.bond@mi6.uk");
        request2.setPassword("007");

        webClient.post()
                .uri(BASE_URL)
                .body(BodyInserters.fromObject(request))
                .exchange()
                .expectStatus().isEqualTo(409);
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
    public void shouldReturnListOfUsernamesMatchingSearchQuery() {
        webClient.get()
                .uri(BASE_URL + "?s=" + getTester().getUsername().substring(0, 2))
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$[0].username").isEqualTo(getTester().getUsername());
    }

    @Test
    public void shouldReturnUsernameByUserId() {
        webClient.get()
                .uri(BASE_URL + "/" + TESTER_ID)
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.username").isEqualTo(getTester().getUsername());
    }

    @Test
    public void shouldReturnNotFoundStatusWhenUserWithGivenIdDoesNotExist() {
        webClient.get()
                .uri(BASE_URL + "/SOME_USER_ID")
                .exchange()
                .expectStatus().isNotFound();
    }

}