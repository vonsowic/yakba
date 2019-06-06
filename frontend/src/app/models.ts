export class Board {
  public id: string;
  public name: string;
  public columns: Column[] = [];
}

export class Column {
  public id: string;
  public cards: Card[] = [];
}

export class Card {
  public id: string;
}
