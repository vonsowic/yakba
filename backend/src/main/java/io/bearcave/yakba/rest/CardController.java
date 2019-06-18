package io.bearcave.yakba.rest;

import io.bearcave.yakba.dto.CardOrderUpdateDTO;
import io.bearcave.yakba.exceptions.BadRequest;
import io.bearcave.yakba.models.Card;
import io.bearcave.yakba.services.CardService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/board/{boardId}/card")
public class CardController {

    private final CardService cardService;

    @Autowired
    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @GetMapping("/{cardId}")
    public Mono<Card> getCardById(
            @PathVariable String boardId,
            @PathVariable String cardId,
            Principal user) {
        return cardService.findCardByIdForUser(boardId, cardId, user.getName());
    }

    @PostMapping
    public Mono<Card> createCard(
            @PathVariable String boardId,
            @RequestBody Map<String, String> body,
            Principal user
    ) {
        var title = body.get("title");
        if (StringUtils.isEmpty(title)) {
            throw new BadRequest("title cannot be empty");
        }

        var columnId = body.get("columnId");
        if (StringUtils.isEmpty(columnId)) {
            throw new BadRequest("columnId cannot be empty");
        }

        var content = body.get("content");

        var card = new Card();
        card.setTitle(title);
        card.setContent(content);
        card.setCreatedByUserId(user.getName());
        return cardService.addCardToBoardForUser(card, columnId, boardId, user.getName());
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{cardId}")
    public Mono<Void> deleteCardById(
            @PathVariable String boardId,
            @PathVariable String cardId,
            Principal user) {
        return cardService.deleteCardFromBoardForUser(cardId, boardId, user.getName());
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @PutMapping("/{cardId}")
    public Mono<Void> updateCardDetails(
            @PathVariable String boardId,
            @PathVariable String cardId,
            @RequestBody Card updatedCard,
            Principal user) {
        updatedCard.setId(cardId);
        return cardService.updateCardForUser(updatedCard, boardId, user.getName());
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @PutMapping("/{cardId}/order")
    public Mono<Void> moveCard(
            @PathVariable String boardId,
            @PathVariable String cardId,
            @RequestBody CardOrderUpdateDTO body,
            Principal user) {
        return cardService.moveCardFromBoardForUserUsing(cardId, boardId, user.getName(), body);
    }
}
