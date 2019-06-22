export class Board {
  public id: string;
  public name: string;
  public columns: Column[] = [];
}

export class Column {
  public id: string;
  public cards: Card[] = [];
}

export class ColumnOrderUpdateRQ {
  public index: number;
}

export class Card {
  public id: string = '';
  public title: string = '';
  public content: string = '';
}

export class CreateCardRQ extends Card {
  public columnId: string;
}

export class CardOrderUpdateRQ {
  public prevPos: CardPosition = new CardPosition();
  public nextPos: CardPosition = new CardPosition();
}

export class CardPosition {
  public columnId: string;
  public index: number = 0;
}

export class SignUpRQ {
  public username: string;
  public email: string;
  public password: string;
}
