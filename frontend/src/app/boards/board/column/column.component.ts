import {Component, Input, OnInit, ViewChild} from '@angular/core';
import {Column} from "../../../models";
import {CdkDragDrop, moveItemInArray, transferArrayItem} from '@angular/cdk/drag-drop';
import {CardService} from "../../../services/card.service";
import {CardFormSimpleComponent} from "../card-form-simple/card-form-simple.component";

@Component({
  selector: 'app-column',
  templateUrl: './column.component.html',
  styleUrls: ['./column.component.scss']
})
export class ColumnComponent implements OnInit {

  @Input()
  public column: Column;

  @ViewChild(CardFormSimpleComponent, {static: false})
  public addCardForm: CardFormSimpleComponent;

  constructor(private cardService: CardService) {
  }

  ngOnInit() {
    if (!this.column.cards) {
      this.column.cards = [];
    }
  }

  drop(event: CdkDragDrop<string[]>) {
    console.log(event);
    if (event.previousContainer === event.container) {
      moveItemInArray(event.container.data, event.previousIndex, event.currentIndex);
    } else {
      transferArrayItem(
        event.previousContainer.data,
        event.container.data,
        event.previousIndex,
        event.currentIndex);
    }

  }

  showAddCardForm() {
    this.addCardForm.show = true;
  }
}
