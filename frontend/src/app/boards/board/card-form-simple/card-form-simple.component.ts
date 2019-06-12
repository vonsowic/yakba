import {Component, Input, OnInit} from '@angular/core';
import {CardService} from "../../../services/card.service";
import {CreateCardRQ} from "../../../models";
import {ActivatedRoute} from "@angular/router";
import {map} from "rxjs/operators";

@Component({
  selector: 'app-card-form-simple',
  templateUrl: './card-form-simple.component.html',
  styleUrls: ['./card-form-simple.component.scss']
})
export class CardFormSimpleComponent implements OnInit {

  @Input()
  public columnId: string;

  @Input()
  public show = false;

  private cardTitle: string = '';

  constructor(
    private cardService: CardService,
    private route: ActivatedRoute
  ) {
  }

  ngOnInit() {
  }

  public addCard() {
    const createCardRQ = new CreateCardRQ();
    createCardRQ.columnId = this.columnId;
    createCardRQ.title = this.cardTitle;

    this.route.params
      .pipe(
        map(params => params['boardId'])
      )
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
}
