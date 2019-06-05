package io.bearcave.yakba.rest;

import io.bearcave.yakba.AbstractIntegrationTest;
import io.bearcave.yakba.dao.BoardRepository;
import io.bearcave.yakba.models.Board;
import io.bearcave.yakba.models.Column;
import org.bson.types.ObjectId;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.is;

class ColumnControllerTest extends AbstractIntegrationTest {

    private Board board;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private WebTestClient webClient;
    private int columnCounter = 0;

    @BeforeEach
    @Override
    public void setUp() {
        super.setUp();
        this.board = new Board("XXX", TESTER_ID);
        boardRepository.save(board)
                .block();
    }

    @WithMockUser(value = AbstractIntegrationTest.TESTER_ID)
    @Test
    void shouldAppendColumnToBoard() {
        var columnName = "TODO Column";
        var requestBody = Collections.singletonMap("name", columnName);

        webClient.post()
                .uri(getEndpointPath())
                .body(BodyInserters.fromObject(requestBody))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.name").isEqualTo(columnName)
                .jsonPath("$.cards").doesNotHaveJsonPath()
                .jsonPath("$.prevColId").doesNotHaveJsonPath();
    }

    @WithMockUser(value = AbstractIntegrationTest.TESTER_ID)
    @Test
    void shouldDeleteColumn() {
        var col = column();
        board.setColumns(Collections.singletonList(col));
        boardRepository.save(board).block();

        webClient.delete()
                .uri(getEndpointPath() + "/" + col.getId())
                .exchange()
                .expectStatus().isNoContent();

        var boardWithDeletedColumn = boardRepository.findById(board.getId()).block();
        var isColumnDeleted = boardWithDeletedColumn.getColumns()
                .stream()
                .map(Column::getId)
                .noneMatch(columnId -> columnId.equals(col.getId()));

        Assert.assertThat(isColumnDeleted, is(true));
    }

    @WithMockUser(value = AbstractIntegrationTest.TESTER_ID)
    @Test
    void returnsNotFoundWhenBoardDoesNotExist() {
        var columnName = "TODO Column";
        var requestBody = Collections.singletonMap("name", columnName);

        webClient.post()
                .uri(getEndpointPath() + "iasbdfi")
                .body(BodyInserters.fromObject(requestBody))
                .exchange()
                .expectStatus().isNotFound();
    }

    @WithMockUser(value = AbstractIntegrationTest.TESTER_ID)
    @Test
    void shouldReturnNotFoundStatusIfColumnDoesNotExist() {
        webClient.delete()
                .uri(getEndpointPath() + "/unexistingColId")
                .exchange()
                .expectStatus().isNotFound();
    }

    @WithMockUser(value = AbstractIntegrationTest.TESTER_ID)
    @Test
    void shouldChangeOrderOfColumnsByPuttingLastElementInTheMiddle() {
        var col0 = column();
        var col1 = column();
        var col2 = column();
        board.setColumns(List.of(col0, col1, col2));
        boardRepository.save(board).block();

        var reorderRequest = Collections.singletonMap("index", 1);

        webClient.put()
                .uri(getEndpointPath() + "/" + col2.getId())
                .body(BodyInserters.fromObject(reorderRequest))
                .exchange()
                .expectStatus().isAccepted();

        var reorderedBoard = boardRepository.findById(board.getId()).block();
        Assert.assertThat(reorderedBoard.getColumns(), Matchers.contains(col0, col2, col1));
    }

    @WithMockUser(value = AbstractIntegrationTest.TESTER_ID)
    @Test
    void shouldChangeOrderOfColumnsByPuttingFirstElementInTheMiddle() {
        var col0 = column();
        var col1 = column();
        var col2 = column();
        board.setColumns(List.of(col0, col1, col2));
        boardRepository.save(board).block();

        var reorderRequest = Collections.singletonMap("index", 1);

        webClient.put()
                .uri(getEndpointPath() + "/" + col0.getId())
                .body(BodyInserters.fromObject(reorderRequest))
                .exchange()
                .expectStatus().isAccepted();

        var reorderedBoard = boardRepository.findById(board.getId()).block();
        Assert.assertThat(reorderedBoard.getColumns(), Matchers.contains(col1, col0, col2));
    }

    @WithMockUser(value = AbstractIntegrationTest.TESTER_ID)
    @Test
    void shouldChangeOrderOfColumnsByPuttingFirstElementAtTheEnd() {
        var col0 = column();
        var col1 = column();
        var col2 = column();
        board.setColumns(List.of(col0, col1, col2));
        boardRepository.save(board).block();

        var reorderRequest = Collections.singletonMap("index", 2);

        webClient.put()
                .uri(getEndpointPath() + "/" + col0.getId())
                .body(BodyInserters.fromObject(reorderRequest))
                .exchange()
                .expectStatus().isAccepted();

        var reorderedBoard = boardRepository.findById(board.getId()).block();
        Assert.assertThat(reorderedBoard.getColumns(), Matchers.contains(col1, col2, col0));
    }

    @WithMockUser(value = AbstractIntegrationTest.TESTER_ID)
    @Test
    void shouldChangeOrderOfColumnsByPuttingLastElementAtTheBeginning() {
        var col0 = column();
        var col1 = column();
        var col2 = column();
        board.setColumns(List.of(col0, col1, col2));
        boardRepository.save(board).block();

        var reorderRequest = Collections.singletonMap("index", 0);

        webClient.put()
                .uri(getEndpointPath() + "/" + col2.getId())
                .body(BodyInserters.fromObject(reorderRequest))
                .exchange()
                .expectStatus().isAccepted();

        var reorderedBoard = boardRepository.findById(board.getId()).block();
        Assert.assertThat(reorderedBoard.getColumns(), Matchers.contains(col2, col0, col1));
    }

    @WithMockUser(value = AbstractIntegrationTest.TESTER_ID)
    @Test
    void shouldReturnBadRequestWhenIndexIsMinus() {
        var col0 = column();
        board.setColumns(List.of(col0));
        boardRepository.save(board).block();

        var reorderRequest = Collections.singletonMap("index", -2);

        webClient.put()
                .uri(getEndpointPath() + "/" + col0.getId())
                .body(BodyInserters.fromObject(reorderRequest))
                .exchange()
                .expectStatus().isBadRequest();
    }

    @WithMockUser(value = AbstractIntegrationTest.TESTER_ID)
    @Test
    void shouldReturnBadRequestWhenIndexIsBiggerThanPossible() {
        var col0 = column();
        board.setColumns(List.of(col0));
        boardRepository.save(board).block();

        var reorderRequest = Collections.singletonMap("index", 1);

        webClient.put()
                .uri(getEndpointPath() + "/" + col0.getId())
                .body(BodyInserters.fromObject(reorderRequest))
                .exchange()
                .expectStatus().isBadRequest();
    }

    private String getEndpointPath() {
        return "/api/board/" + this.board.getId() + "/column";
    }

    private Column column() {
        var col = new Column();
        col.setId(ObjectId.get().toString());
        col.setName("col" + columnCounter++);
        return col;
    }
}