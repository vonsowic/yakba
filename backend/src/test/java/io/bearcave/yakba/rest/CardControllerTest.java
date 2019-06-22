package io.bearcave.yakba.rest;

import io.bearcave.yakba.AbstractIntegrationTest;
import io.bearcave.yakba.dao.BoardRepository;
import io.bearcave.yakba.dto.CardOrderUpdateRQ;
import io.bearcave.yakba.dto.CardPosition;
import io.bearcave.yakba.models.Board;
import io.bearcave.yakba.models.Card;
import io.bearcave.yakba.models.Column;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

@WithMockUser(value = AbstractIntegrationTest.TESTER_ID)
class CardControllerTest extends AbstractIntegrationTest {

    private Board board;
    private int columnCounter = 0;
    private Column column1;
    private Column column2;
    private Column column3;

    @Autowired
    private BoardRepository boardRepository;

    @BeforeEach
    @Override
    public void setUp() {
        super.setUp();
        this.board = new Board("TEST", TESTER_ID);
        this.column1 = column();
        this.column2 = column();
        this.column3 = column();
        this.board.setColumns(List.of(column1, column2, column3));
        updateTestBoard();
    }

    @Test
    void shouldGetOneCardByBoardAndCardId() {
        var card = card("Some task", "Description of the task");
        column1.setCards(List.of(card));
        updateTestBoard();

        webClient.get()
                .uri(getEndpointPath() + "/" + card.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("id").isEqualTo(card.getId())
                .jsonPath("title").isEqualTo("Some task")
                .jsonPath("content").isEqualTo("Description of the task");
    }

    @Test
    void shouldReturnNotFoundWhenCardDoesNotExist() {
        webClient.get()
                .uri(getEndpointPath() + "/CARD_ID")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void shouldReturnForbiddenStatusWhenUserIsNotMemberOfBoard() {
        var card = card("Access test", "Tester should not have access to this card");
        var column = new Column();
        column.setCards(List.of(card));
        var anotherBoard = new Board();
        anotherBoard.setColumns(List.of(column));
        boardRepository.save(anotherBoard).block();

        webClient.get()
                .uri("/api/board/" + anotherBoard.getId() + "/card/" + card.getId())
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    void shouldCreateNewCardAndAddAtBeginningOfColumn() {
        var requestBody = Map.of(
                "title", "First task",
                "columnId", column2.getId()
        );

        webClient.post()
                .uri(getEndpointPath())
                .body(BodyInserters.fromObject(requestBody))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("id").isNotEmpty()
                .jsonPath("title").isEqualTo("First task")
                .jsonPath("content").doesNotHaveJsonPath();
    }

    @Test
    void shouldNotCreateCardWithoutName() {
        var requestBody = Map.of(
                "title", ""
        );

        webClient.post()
                .uri(getEndpointPath())
                .body(BodyInserters.fromObject(requestBody))
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void shouldNotCreateCardWhenColumnDoesNotExist() {
        var requestBody = Map.of(
                "title", "First task",
                "columnId", "someColId"
        );

        webClient.post()
                .uri(getEndpointPath())
                .body(BodyInserters.fromObject(requestBody))
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void shouldNotCreatCardWhenBoardDoesNotExist() {
        var requestBody = Map.of(
                "title", "First task",
                "columnId", column3.getId()
        );

        webClient.post()
                .uri("/api/board/UNEXISTING_BOARD_ID/card")
                .body(BodyInserters.fromObject(requestBody))
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void shouldDeleteCard() {
        var card = card("Card to be deleted");
        column2.setCards(List.of(card));
        updateTestBoard();

        webClient.delete()
                .uri(getEndpointPath() + "/" + card.getId())
                .exchange()
                .expectStatus().isNoContent();

        assertThat(getCardById(card.getId()), is(nullValue()));
    }

    @Test
    void shouldNotDeleteCardWhenBoardDoesNotExist() {
        var card = card("Card to be deleted");
        column2.setCards(List.of(card));
        updateTestBoard();

        webClient.delete()
                .uri("/api/board/UNEXISTING_BOARD_ID/card/" + card.getId())
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void shouldNotDeleteCardIfUserIsNotMemberOfAssociatedBoard() {
        var card = card("Access test", "Tester should not have access to this card");
        var column = new Column();
        column.setCards(List.of(card));
        var anotherBoard = new Board();
        anotherBoard.setColumns(List.of(column));
        boardRepository.save(anotherBoard).block();

        webClient.delete()
                .uri("/api/board/" + anotherBoard.getId() + "/card/" + card.getId())
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    void shouldUpdateCardDetails() {
        var card = card("Task");
        column2.setCards(List.of(card));
        updateTestBoard();

        var requestBody = Map.of(
                "content", "updated description"
        );

        webClient.put()
                .uri(getEndpointPath() + "/" + card.getId())
                .body(BodyInserters.fromObject(requestBody))
                .exchange()
                .expectStatus().isAccepted();

        assertThat(getCardById(card.getId()), hasProperty("content", equalTo("updated description")));
    }

    @Test
    void shouldMoveCardInOneColumn() {
        var card1 = card("Task 1");
        var card2 = card("Task 2");
        var card3 = card("Task 3");
        column1.setCards(List.of(card1, card2, card3));
        updateTestBoard();

        var prevPos = new CardPosition();
        prevPos.setColumnId(column1.getId());
        prevPos.setIndex(0);

        var nextPos = new CardPosition();
        nextPos.setColumnId(column1.getId());
        nextPos.setIndex(2);

        var request = new CardOrderUpdateRQ();
        request.setPrevPos(prevPos);
        request.setNextPos(nextPos);

        webClient.put()
                .uri(getEndpointPath() + "/" + card1.getId() + "/order")
                .body(BodyInserters.fromObject(request))
                .exchange()
                .expectStatus().isAccepted();

        var updatedColumn = boardRepository.findById(board.getId())
                .block()
                .getColumns()
                .get(0);
        assertThat(updatedColumn.getCards(), contains(card2, card3, card1));
    }

    @Test
    void shouldMoveCardToDifferentColumn() {
        var card1 = card("Task 1");
        var card2 = card("Task 2");
        var card3 = card("Task 3");
        column1.setCards(List.of(card1, card2, card3));
        updateTestBoard();

        var prevPos = new CardPosition();
        prevPos.setColumnId(column1.getId());
        prevPos.setIndex(1);

        var nextPos = new CardPosition();
        nextPos.setColumnId(column2.getId());

        var request = new CardOrderUpdateRQ();
        request.setPrevPos(prevPos);
        request.setNextPos(nextPos);

        webClient.put()
                .uri(getEndpointPath() + "/" + card2.getId() + "/order")
                .body(BodyInserters.fromObject(request))
                .exchange()
                .expectStatus().isAccepted();

        var updatedColumn1 = boardRepository.findById(board.getId())
                .block()
                .getColumns()
                .get(0);
        assertThat(updatedColumn1.getCards(), contains(card1, card3));

        var updatedColumn2 = boardRepository.findById(board.getId())
                .block()
                .getColumns()
                .get(1);
        assertThat(updatedColumn2.getCards(), contains(card2));
    }

    @Test
    void shouldReturnConflictStatusWhenInTheMeantimeCardHasBeenMovedBySomebodyElse() {
        var card1 = card("Task 1");
        var card2 = card("Task 2");
        column1.setCards(List.of(card1, card2));
        updateTestBoard();

        var prevPos = new CardPosition();
        prevPos.setColumnId(column1.getId());
        prevPos.setIndex(1);

        var nextPos = new CardPosition();
        nextPos.setColumnId(column1.getId());

        var request = new CardOrderUpdateRQ();
        request.setPrevPos(prevPos);
        request.setNextPos(nextPos);

        webClient.put()
                .uri(getEndpointPath() + "/" + card1.getId() + "/order")
                .body(BodyInserters.fromObject(request))
                .exchange()
                .expectStatus().isEqualTo(409);
    }

    private String getEndpointPath() {
        return "/api/board/" + this.board.getId() + "/card";
    }

    private Column column() {
        var col = new Column();
        col.setId(ObjectId.get().toString());
        col.setName("col" + columnCounter++);
        return col;
    }

    private Card card(String title) {
        return card(title, null);
    }

    private Card card(String title, String content) {
        var card = new Card();
        card.setId(ObjectId.get().toString());
        card.setTitle(title);
        card.setContent(content);
        return card;
    }

    private void updateTestBoard() {
        boardRepository.save(board)
                .block();
    }

    private Card getCardById(String cardId) {
        return boardRepository.findById(board.getId())
                .map(board -> board.getColumns()
                        .stream()
                        .map(Column::getCards)
                        .flatMap(Collection::stream)
                        .filter(card -> card.idEquals(cardId))
                        .findFirst())
                .flatMap(Mono::justOrEmpty)
                .block();
    }
}