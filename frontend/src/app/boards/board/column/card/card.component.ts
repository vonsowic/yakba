import {Component, Input, OnInit} from '@angular/core';
import {Card} from "../../../../models";

@Component({
  selector: 'app-card',
  templateUrl: './card.component.html',
  styleUrls: ['./card.component.scss', '../column.component.scss']
})
export class CardComponent implements OnInit {

  @Input()
  public card: Card;

  constructor() {
  }

  ngOnInit() {
  }

}
