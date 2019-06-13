import {Injectable} from '@angular/core';
import {BehaviorSubject, Observable} from "rxjs";
import {Card, CardOrderUpdateRQ, CreateCardRQ} from "../models";
import {HttpClient} from "@angular/common/http";
import {BoardsService} from "./boards.service";
import {tap} from "rxjs/operators";
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

  public getCardDetails(boardId: string, cardId: string): Observable<Card> {
    return this.http.get<Card>(`${this.endpointBasePath}/${boardId}/card/${cardId}`);
  }

  public createNewCard(boardId: string, card: CreateCardRQ): Observable<Card> {
    return this.http.post<Card>(`${this.endpointBasePath}/${boardId}/card`, card)
      .pipe(
        tap(newCard => this.boardService.pushCardToBoard(Object.assign({}, card, newCard) as CreateCardRQ)),
        tap(newCard => this._selectedCard.next(newCard))
      );
  }

  public updateCard(boardId: string, updatedCard: Card): Observable<void> {
    return this.http.put<void>(`${this.endpointBasePath}/${boardId}/card/${updatedCard.id}`, updatedCard)
      .pipe(
        tap(() => {
          this.boardService.updateCard(updatedCard);
        })
      );
  }

  public moveCard(boardId: string, cardId: string, cardOrderUpdateRQ: CardOrderUpdateRQ): Observable<void> {
    return this.http.put<void>(`${this.endpointBasePath}/${boardId}/card/${cardId}`, cardOrderUpdateRQ);
  }

  public deleteCard(boardId: string, cardId: string): Observable<void> {
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

  handleCardDrop(event: CdkDragDrop<string[]>) {
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
}
