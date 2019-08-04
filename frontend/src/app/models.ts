export class Board {
  id: string;
  name: string;
  columns: Column[] = [];
}

export class Column {
  id: string;
  name: string;
  cards: Card[] = [];
}

export class ColumnOrderUpdateRQ {
  index: number;
}

export class Card {
  id: string = '';
  title: string = '';
  content: string = '';
}

export class CreateCardRQ extends Card {
  columnId: string;
}

export class CardOrderUpdateRQ {
  prevPos: CardPosition = new CardPosition();
  nextPos: CardPosition = new CardPosition();
}

export class CardPosition {
  columnId: string;
  index: number = 0;
}

export class SignUpRQ {
  username: string;
  email: string;
  password: string;
}

export class UserBoardAccessRS {
  userId: string;
  accessLevel: 'USER' | 'ADMIN'
}
