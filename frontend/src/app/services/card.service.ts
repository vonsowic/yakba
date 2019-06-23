import {Injectable} from '@angular/core';
import {BehaviorSubject, Observable, of} from "rxjs";
import {Card, CardOrderUpdateRQ, CreateCardRQ} from "../models";
import {HttpClient} from "@angular/common/http";
import {BoardsService} from "./boards.service";
import {catchError, tap} from "rxjs/operators";
import {CdkDragDrop, moveItemInArray, transferArrayItem} from "@angular/cdk/drag-drop";

@Injectable({
  providedIn: 'root'
})
export class CardService {

  private readonly endpointBasePath = '/api/board';

  private _selectedCard = new BehaviorSubject<Card>(new Card());
  private selectedCard = this._selectedCard.asObservable();

  constructor(private http: HttpClient, private boardService: BoardsService) {
  }

  getCardDetails(boardId: string, cardId: string): Observable<Card> {
    return this.http.get<Card>(`${this.endpointBasePath}/${boardId}/card/${cardId}`);
  }

  createNewCard(boardId: string, card: CreateCardRQ): Observable<Card> {
    return this.http.post<Card>(`${this.endpointBasePath}/${boardId}/card`, card)
      .pipe(
        tap(newCard => this.boardService.pushCardToBoard(Object.assign({}, card, newCard) as CreateCardRQ)),
        tap(newCard => this._selectedCard.next(newCard))
      );
  }

  updateCard(boardId: string, updatedCard: Card): Observable<void> {
    return this.http.put<void>(`${this.endpointBasePath}/${boardId}/card/${updatedCard.id}`, updatedCard)
      .pipe(
        tap(() => {
          this.boardService.updateCard(updatedCard);
        })
      );
  }

  moveCard(boardId: string, cardId: string, cardOrderUpdateRQ: CardOrderUpdateRQ): Observable<void> {
    return this.http.put<void>(`${this.endpointBasePath}/${boardId}/card/${cardId}`, cardOrderUpdateRQ);
  }

  deleteCard(boardId: string, cardId: string): Observable<void> {
    return this.http.delete<void>(`${this.endpointBasePath}/${boardId}/card/${cardId}`)
      .pipe(
        tap(() => {
          return this.boardService.removeCard(cardId);
        })
      );
  }

  getSelectedCard(): Observable<Card> {
    return this.selectedCard;
  }

  selectCard(boardId: string, card: Card) {
    this._selectedCard.next(card);

    this.getCardDetails(boardId, card.id)
      .subscribe(detailedCard => {
        this._selectedCard.next(detailedCard);
      });
  }

  deselectCard() {
    this._selectedCard.next(new Card());
  }

  handleCardDrop(boardId: string, event: CdkDragDrop<string[]>): Observable<void> {
    const request = new CardOrderUpdateRQ();
    request.prevPos.index = event.previousIndex;
    request.prevPos.columnId = event.previousContainer.id;
    request.nextPos.index = event.currentIndex;
    request.nextPos.columnId = event.container.id;
    const cardId = (event.previousContainer.data[request.prevPos.index]['id']);

    if (event.previousContainer === event.container) {
      moveItemInArray(event.container.data, event.previousIndex, event.currentIndex);
    } else {
      transferArrayItem(
        event.previousContainer.data,
        event.container.data,
        event.previousIndex,
        event.currentIndex);
    }

    return this.http.put<void>(`/api/board/${boardId}/card/${cardId}/order`, request)
      .pipe(
        catchError(err => {
          if (err.status !== 409) {
            return of(err)
          }

          if (event.previousContainer === event.container) {
            moveItemInArray(event.container.data, event.currentIndex, event.previousIndex);
          } else {
            transferArrayItem(
              event.container.data,
              event.previousContainer.data,
              event.currentIndex,
              event.previousIndex);
          }
        })
      )
  }
}
