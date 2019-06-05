package io.bearcave.yakba.rest;

import io.bearcave.yakba.dto.ColumnReorderRequestDTO;
import io.bearcave.yakba.exceptions.BadRequest;
import io.bearcave.yakba.models.Column;
import io.bearcave.yakba.services.ColumnService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/board/{boardId}/column")
public class ColumnController {

    private final ColumnService columnService;

    @Autowired
    public ColumnController(ColumnService columnService) {
        this.columnService = columnService;
    }

    @PostMapping
    public Mono<Column> createNewColumn(
            @RequestBody Map<String, String> body,
            @PathVariable String boardId,
            Principal user) {
        var columnName = body.get("name");
        if (StringUtils.isEmpty(columnName)) {
            throw new BadRequest();
        }

        return columnService.addColumnToBoardAsUser(columnName, boardId, user.getName());
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{columnId}")
    public Mono<Void> deleteColumn(
            @PathVariable String boardId,
            @PathVariable String columnId,
            Principal user) {
        return columnService.removeColumnFromBoardAsUser(columnId, boardId, user.getName());
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @PutMapping("/{columnId}")
    public Mono<Void> moveColumnOnRequestIndex(
            @PathVariable String boardId,
            @PathVariable String columnId,
            @RequestBody Map<String, Integer> body,
            Principal user) {
        var index = body.get("index");
        if (index == null) {
            throw new BadRequest();
        }

        var reorderRequest = new ColumnReorderRequestDTO();
        reorderRequest.setBoardId(boardId);
        reorderRequest.setColumnId(columnId);
        reorderRequest.setIndex(index);

        return columnService.reorderColumnAsUser(reorderRequest, user.getName());
    }
}
