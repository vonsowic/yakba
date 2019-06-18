package io.bearcave.yakba.services;

import io.bearcave.yakba.dao.BoardRepository;
import io.bearcave.yakba.dto.CardOrderUpdateRQ;
import io.bearcave.yakba.exceptions.BadRequest;
import io.bearcave.yakba.exceptions.Conflict;
import io.bearcave.yakba.exceptions.NotFound;
import io.bearcave.yakba.models.Board;
import io.bearcave.yakba.models.Card;
import io.bearcave.yakba.models.Column;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Optional;

@Service
public class CardService {

    private final BoardRepository boardRepository;
    private final BoardService boardService;

    @Autowired
    public CardService(BoardRepository boardRepository, BoardService boardService) {
        this.boardRepository = boardRepository;
        this.boardService = boardService;
    }

    public Mono<Card> findCardByIdForUser(String boardId, String cardId, String userId) {
        return boardService.getBoardForUser(boardId, userId)
                .map(Board::getColumns)
                .map(Collection::stream)
                .map(columns -> columns.flatMap(column -> column.getCards().stream()))
                .flatMap(card -> card
                        .filter(c -> c.getId().equals(cardId))
                        .findFirst()
                        .map(Mono::just)
                        .orElseThrow(NotFound::new));
    }

    public Mono<Card> addCardToBoardForUser(Card card, String columnId, String boardId, String userId) {
        return boardService.getBoardForUser(boardId, userId)
                .flatMap(board -> {
                    var column = getColumnById(board, columnId)
                            .orElseThrow(() -> new NotFound(String.format(
                                    "Column %s does not exist in board %s[%s]", columnId, board.getName(), board.getId())
                            ));

                    card.setId(ObjectId.get().toString());
                    column.getCards()
                            .add(0, card);

                    return boardRepository.save(board)
                            .then(Mono.just(card));
                });
    }

    public Mono<Void> deleteCardFromBoardForUser(String cardId, String boardId, String userId) {
        return boardService.getBoardForUser(boardId, userId)
                .flatMap(board -> {
                    var column = board.getColumns()
                            .stream()
                            .filter(c -> c.getCards()
                                    .stream()
                                    .anyMatch(card -> card.idEquals(cardId)))
                            .findFirst()
                            .orElseThrow(NotFound::new);

                    column.getCards().removeIf(card -> card.idEquals(cardId));
                    return boardRepository.save(board);
                })
                .then();
    }

    public Mono<Void> updateCardForUser(Card updatedCard, String boardId, String userId) {
        return boardService.getBoardForUser(boardId, userId)
                .flatMap(board -> {
                    var dbCard = getCardById(board, updatedCard.getId())
                            .orElseThrow(() -> new NotFound(String.format(
                                    "Card %s does not exist in board %s[%s]", updatedCard.getId(), board.getName(), board.getId())
                            ));

                    if (StringUtils.isNotEmpty(updatedCard.getTitle())) {
                        dbCard.setTitle(updatedCard.getTitle());
                    }

                    if (StringUtils.isNotEmpty(updatedCard.getContent())) {
                        dbCard.setContent(updatedCard.getContent());
                    }

                    return boardRepository.save(board);
                })
                .then();
    }

    public Mono<Void> moveCardFromBoardForUserUsing(String cardId, String boardId, String userId, CardOrderUpdateRQ orderUpdate) {
        return boardService.getBoardForUser(boardId, userId)
                .flatMap(board -> {
                    var prevColCards = getColumnById(board, orderUpdate.getPrevPos().getColumnId())
                            .map(Column::getCards)
                            .orElseThrow(Conflict::new);

                    var prevIndex = orderUpdate.getPrevPos().getIndex();
                    if (prevColCards.size() < prevIndex || prevIndex < 0) {
                        return Mono.error(BadRequest::new);
                    }

                    var movedCard = prevColCards.get(prevIndex);
                    if (!movedCard.idEquals(cardId)) {
                        return Mono.error(() -> new Conflict("Card has already been moved"));
                    }

                    prevColCards.remove(prevIndex);

                    var nextColCards = getColumnById(board, orderUpdate.getNextPos().getColumnId())
                            .map(Column::getCards)
                            .orElseThrow(Conflict::new);

                    var nextIndex = orderUpdate.getNextPos().getIndex();
                    if (nextColCards.size() > nextIndex) {
                        nextColCards.add(nextIndex, movedCard);
                    } else {
                        nextColCards.add(movedCard);
                    }

                    return boardRepository.save(board);
                })
                .then();
    }

    private Optional<Column> getColumnById(Board board, String columnId) {
        return board.getColumns()
                .stream()
                .filter(c -> c.getId().equals(columnId))
                .findFirst();
    }

    private Optional<Card> getCardById(Board board, String cardId) {
        return board.getColumns()
                .stream()
                .map(Column::getCards)
                .flatMap(Collection::stream)
                .filter(card -> card.idEquals(cardId))
                .findFirst();

    }
}
