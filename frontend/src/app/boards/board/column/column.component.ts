import {Component, Input, OnInit, ViewChild} from '@angular/core';
import {Card, Column} from "../../../models";
import {CdkDragDrop} from '@angular/cdk/drag-drop';
import {CardService} from "../../../services/card.service";
import {CardFormSimpleComponent} from "../card-form-simple/card-form-simple.component";
import {BoardBaseComponent} from "../../BoardBaseComponent";
import {ActivatedRoute} from "@angular/router";
import {flatMap} from "rxjs/operators";
import {ColumnService} from "../../../services/column.service";

@Component({
  selector: 'app-column',
  templateUrl: './column.component.html',
  styleUrls: ['./column.component.scss']
})
export class ColumnComponent extends BoardBaseComponent implements OnInit {

  @Input()
  public column: Column;

  @ViewChild(CardFormSimpleComponent, {static: false})
  public addCardForm: CardFormSimpleComponent;

  constructor(
    private cardService: CardService,
    private columnService: ColumnService,
    route: ActivatedRoute
  ) {
    super(route);
  }

  ngOnInit() {
    if (!this.column.cards) {
      this.column.cards = [];
    }
  }

  drop(event: CdkDragDrop<string[]>) {
    console.log(event);
    this.cardService.handleCardDrop(event)
  }

  showAddCardForm() {
    this.addCardForm.show = true;
  }

  selectCard(card: Card) {
    this.getBoardId()
      .subscribe(boardId => {
        this.cardService.selectCard(boardId, card)
      })
  }

  moveLeft() {
    this.getBoardId()
      .pipe(
        flatMap(boardId => this.columnService.moveColumnOneLeft(boardId, this.column.id))
      )
      .subscribe(() => {
      })
  }

  moveRight() {
    this.getBoardId()
      .pipe(
        flatMap(boardId => this.columnService.moveColumnOneRight(boardId, this.column.id))
      )
      .subscribe(() => {
      })
  }

  deleteColumn() {
    this.getBoardId()
      .pipe(
        flatMap(boardId => this.columnService.deleteColumn(boardId, this.column.id))
      )
      .subscribe(() => {
      })
  }
}
