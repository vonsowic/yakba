import {Injectable} from '@angular/core';
import {Observable} from "rxjs";
import {Card, CardOrderUpdateRQ, CreateCardRQ} from "../models";
import {HttpClient} from "@angular/common/http";
import {BoardsService} from "./boards.service";
import {tap} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class CardService {

  private readonly endpointBasePath = '/api/board';

  constructor(private http: HttpClient, private boardService: BoardsService) {
  }

  public getCardDetails(boardId: string, cardId: string): Observable<Card> {
    return this.http.get<Card>(`${this.endpointBasePath}/${boardId}/card/${cardId}`);
  }

  public createNewCard(boardId: string, card: CreateCardRQ): Observable<Card> {
    return this.http.post<Card>(`${this.endpointBasePath}/${boardId}/card`, card)
      .pipe(
        tap(newCard => this.boardService.pushCardToBoard(Object.assign({}, card, newCard) as CreateCardRQ))
      );
  }

  public updateCard(boardId: string, card: Card): Observable<void> {
    return this.http.put<void>(`${this.endpointBasePath}/${boardId}/card/${card.id}`, card);
  }

  public moveCard(boardId: string, cardId: string, cardOrderUpdateRQ: CardOrderUpdateRQ): Observable<void> {
    return this.http.put<void>(`${this.endpointBasePath}/${boardId}/card/${cardId}`, cardOrderUpdateRQ);
  }

  public deleteCard(boardId: string, cardId: string): Observable<void> {
    return this.http.delete<void>(`${this.endpointBasePath}/${boardId}/card/${cardId}`);
  }
}
