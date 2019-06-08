package io.bearcave.yakba.services;

import io.bearcave.yakba.dao.BoardRepository;
import io.bearcave.yakba.dto.ColumnReorderRequestDTO;
import io.bearcave.yakba.exceptions.BadRequest;
import io.bearcave.yakba.exceptions.NotFound;
import io.bearcave.yakba.models.Board;
import io.bearcave.yakba.models.Column;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@Service
public class ColumnService {

    private final BoardRepository boardRepository;
    private final BoardService boardService;

    @Autowired
    public ColumnService(BoardRepository boardRepository, BoardService boardService) {
        this.boardRepository = boardRepository;
        this.boardService = boardService;
    }

    public Mono<Column> addColumnToBoardAsUser(String columnName, String boardId, String userId) {
        var newColumn = new Column();
        newColumn.setId(ObjectId.get().toString());
        newColumn.setName(columnName);

        return boardService.getBoardForUser(boardId, userId)
                .flatMap(board -> {
                    board.appendColumn(newColumn);
                    return boardRepository.save(board);
                })
                .map(Board::getLastColumn)
                .map(Optional::orElseThrow);
    }

    public Mono<Void> removeColumnFromBoardAsUser(String columnId, String boardId, String userId) {
        return boardService.getBoardForUser(boardId, userId)
                .flatMap(board -> {
                    board.getColumns()
                            .stream()
                            .filter(column -> column.getId().equals(columnId))
                            .findFirst()
                            .ifPresentOrElse(
                                    column -> board.getColumns().remove(column),
                                    () -> new NotFound(String.format("Column %s does not exist", columnId)));

                    return boardRepository.save(board);
                })
                .then();
    }

    public Mono<Void> reorderColumnAsUser(ColumnReorderRequestDTO reorderRequest, String userId) {
        return boardService.getBoardForUser(reorderRequest.getBoardId(), userId)
                .flatMap(board -> {
                    var columns = board.getColumns();
                    if (isRequestedIndexInvalid(columns, reorderRequest.getIndex())) {
                        throw new BadRequest();
                    }

                    var movedColumnIndex = findIndexById(columns, reorderRequest.getColumnId());
                    var movedColumn = columns.get(movedColumnIndex);
                    columns.remove(movedColumnIndex);

                    columns.add(reorderRequest.getIndex(), movedColumn);

                    return boardRepository.save(board);
                })
                .then();
    }

    private boolean isRequestedIndexInvalid(List<Column> columns, int index) {
        return index >= columns.size() || index < 0;
    }

    private int findIndexById(List<Column> columns, String searchedId) {
        return IntStream.range(0, columns.size())
                .filter(i -> columns.get(i).getId().equals(searchedId))
                .findFirst()
                .orElseThrow(NotFound::new);
    }
}
