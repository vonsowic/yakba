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
import org.springframework.web.reactive.function.BodyInserters;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.is;

class ColumnControllerTest extends AbstractIntegrationTest {

    private Board board;

    @Autowired
    private BoardRepository boardRepository;

    private int columnCounter = 0;

    @BeforeEach
    @Override
    public void setUp() {
        super.setUp();
        this.board = new Board("XXX", TESTER_ID);
        boardRepository.save(board)
                .block();
    }

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

    @Test
    void shouldReturnNotFoundStatusIfColumnDoesNotExist() {
        webClient.delete()
                .uri(getEndpointPath() + "/unexistingColId")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void shouldChangeOrderOfColumnsByPuttingLastElementInTheMiddle() {
        var col0 = column();
        var col1 = column();
        var col2 = column();
        board.setColumns(List.of(col0, col1, col2));
        boardRepository.save(board).block();

        var reorderRequest = Collections.singletonMap("index", 1);

        webClient.put()
                .uri(getEndpointPath() + "/" + col2.getId() + "/order")
                .body(BodyInserters.fromObject(reorderRequest))
                .exchange()
                .expectStatus().isAccepted();

        var reorderedBoard = boardRepository.findById(board.getId()).block();
        Assert.assertThat(reorderedBoard.getColumns(), Matchers.contains(col0, col2, col1));
    }

    @Test
    void shouldChangeOrderOfColumnsByPuttingFirstElementInTheMiddle() {
        var col0 = column();
        var col1 = column();
        var col2 = column();
        board.setColumns(List.of(col0, col1, col2));
        boardRepository.save(board).block();

        var reorderRequest = Collections.singletonMap("index", 1);

        webClient.put()
                .uri(getEndpointPath() + "/" + col0.getId() + "/order")
                .body(BodyInserters.fromObject(reorderRequest))
                .exchange()
                .expectStatus().isAccepted();

        var reorderedBoard = boardRepository.findById(board.getId()).block();
        Assert.assertThat(reorderedBoard.getColumns(), Matchers.contains(col1, col0, col2));
    }

    @Test
    void shouldChangeOrderOfColumnsByPuttingFirstElementAtTheEnd() {
        var col0 = column();
        var col1 = column();
        var col2 = column();
        board.setColumns(List.of(col0, col1, col2));
        boardRepository.save(board).block();

        var reorderRequest = Collections.singletonMap("index", 2);

        webClient.put()
                .uri(getEndpointPath() + "/" + col0.getId() + "/order")
                .body(BodyInserters.fromObject(reorderRequest))
                .exchange()
                .expectStatus().isAccepted();

        var reorderedBoard = boardRepository.findById(board.getId()).block();
        Assert.assertThat(reorderedBoard.getColumns(), Matchers.contains(col1, col2, col0));
    }

    @Test
    void shouldChangeOrderOfColumnsByPuttingLastElementAtTheBeginning() {
        var col0 = column();
        var col1 = column();
        var col2 = column();
        board.setColumns(List.of(col0, col1, col2));
        boardRepository.save(board).block();

        var reorderRequest = Collections.singletonMap("index", 0);

        webClient.put()
                .uri(getEndpointPath() + "/" + col2.getId() + "/order")
                .body(BodyInserters.fromObject(reorderRequest))
                .exchange()
                .expectStatus().isAccepted();

        var reorderedBoard = boardRepository.findById(board.getId()).block();
        Assert.assertThat(reorderedBoard.getColumns(), Matchers.contains(col2, col0, col1));
    }

    @Test
    void shouldReturnBadRequestWhenIndexIsMinus() {
        var col0 = column();
        board.setColumns(List.of(col0));
        boardRepository.save(board).block();

        var reorderRequest = Collections.singletonMap("index", -2);

        webClient.put()
                .uri(getEndpointPath() + "/" + col0.getId() + "/order")
                .body(BodyInserters.fromObject(reorderRequest))
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void shouldReturnBadRequestWhenIndexIsBiggerThanPossible() {
        var col0 = column();
        board.setColumns(List.of(col0));
        boardRepository.save(board).block();

        var reorderRequest = Collections.singletonMap("index", 1);

        webClient.put()
                .uri(getEndpointPath() + "/" + col0.getId() + "/order")
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