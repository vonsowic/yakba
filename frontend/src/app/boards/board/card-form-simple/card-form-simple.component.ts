import {Component, HostListener, Input, OnInit} from '@angular/core';
import {CardService} from "../../../services/card.service";
import {CreateCardRQ} from "../../../models";
import {ActivatedRoute} from "@angular/router";
import {BoardBaseComponent} from "../../BoardBaseComponent";

@Component({
  selector: 'app-card-form-simple',
  templateUrl: './card-form-simple.component.html',
  styleUrls: ['./card-form-simple.component.scss']
})
export class CardFormSimpleComponent extends BoardBaseComponent implements OnInit {

  @Input()
  public columnId: string;

  @Input()
  public show = false;

  private cardTitle: string = '';

  constructor(
    private cardService: CardService,
    route: ActivatedRoute
  ) {
    super(route)
  }

  ngOnInit() {
  }

  public addCard() {
    const createCardRQ = new CreateCardRQ();
    createCardRQ.columnId = this.columnId;
    createCardRQ.title = this.cardTitle;

    this.getBoardId()
      .subscribe(boardId => {
        return this.cardService.createNewCard(boardId, createCardRQ)
          .subscribe(
            res => {
              this.cardTitle = '';
              this.show = false;
            },
            err => {
            }
          )
      })
  }

  public hide() {
    this.show = false;
  }

  @HostListener('document:keydown', ['$event']) onKeydownHandler(event: KeyboardEvent) {
    if (event.key === "Escape") {
      this.hide();
    }
  }
}
