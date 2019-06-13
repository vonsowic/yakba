import {Component, OnInit} from '@angular/core';
import {CardService} from "../../../services/card.service";
import {Card} from "../../../models";
import {BoardBaseComponent} from "../../BoardBaseComponent";
import {ActivatedRoute} from "@angular/router";
import {flatMap} from "rxjs/operators";

@Component({
  selector: 'app-card-form',
  templateUrl: './card-form.component.html',
  styleUrls: ['./card-form.component.scss']
})
export class CardFormComponent extends BoardBaseComponent implements OnInit {

  private card: Card = new Card();

  constructor(
    private cardService: CardService,
    route: ActivatedRoute
  ) {
    super(route);
  }

  ngOnInit() {
    this.cardService.getSelectedCard()
      .subscribe(card => {
        this.card = card;
      })
  }

  updateCard() {
    this.getBoardId()
      .pipe(flatMap(boardId => this.cardService.updateCard(boardId, this.card)))
      .subscribe(() => {
        this.close();

      })
  }

  deleteCard() {
    this.getBoardId()
      .pipe(flatMap(boardId => this.cardService.deleteCard(boardId, this.card.id)))
      .subscribe(() => {
        this.close();
      })
  }

  close() {
    this.cardService.deselectCard();
  }

}
