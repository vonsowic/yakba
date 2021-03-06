import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Board, Card, Column, CreateCardRQ, UserBoardAccessRS} from "../models";
import {BehaviorSubject, Observable, of} from "rxjs";
import {NavigationService} from "./navigation.service";
import {catchError, map} from "rxjs/operators";
import arrayMove from "array-move";

@Injectable({
  providedIn: 'root'
})
export class BoardsService {

  private readonly endpoint = '/api/board';

  private _currentBoard = new BehaviorSubject<Board>(null);
  private currentBoard = this._currentBoard.asObservable();

  constructor(
    private http: HttpClient,
    private navigationService: NavigationService
  ) {
  }

  getBoards(): Observable<Board[]> {
    return this.http.get<Board[]>(this.endpoint);
  }

  createNewBoard(name: string): Observable<Board> {
    return this.http.post<Board>(this.endpoint, {
      name
    })
  }

  getBoard(boardId: string): Observable<Board> {
    this.http.get<Board>(this.endpoint + '/' + boardId)
      .pipe(
        map(board => Object.assign({columns: []}, board)),
        catchError(err => {
          if (err.status === 404) {
            return this.navigationService.goToNotFound();
          }

          return of(err);
        })
      )
      .subscribe(board => {
        this._currentBoard.next(board)
      });

    return this.currentBoard;
  }

  pushColumnToBoard(column: Column) {
    this._currentBoard.getValue()
      .columns
      .push(column);

    this._currentBoard.next(this._currentBoard.getValue())
  }

  pushCardToBoard(card: CreateCardRQ) {
    const column = this._currentBoard.getValue()
      .columns
      .find(column => column.id === card.columnId);

    column.cards = [card, ...column.cards];
    this._currentBoard.next(this._currentBoard.getValue())
  }

  removeCard(cardId: string) {
    const column = this._currentBoard.getValue()
      .columns
      .find(column => column.cards.find(card => card.id === cardId) !== undefined);

    column.cards = column.cards.filter(card => card.id !== cardId);
    this._currentBoard.next(this._currentBoard.getValue())
  }

  removeColumn(columnId: string) {
    const board = this._currentBoard.getValue();
    board.columns = board.columns.filter(column => column.id !== columnId);
    this._currentBoard.next(board)
  }

  updateCard(updatedCard: Card) {
    for (let col of this._currentBoard.getValue().columns) {
      for (let card of col.cards) {
        if (card.id === updatedCard.id) {
          Object.assign(card, updatedCard);
          this._currentBoard.next(this._currentBoard.getValue());
          return;
        }
      }
    }
  }

  clearBoard() {
    this._currentBoard.next(null);
  }

  refreshBoard() {
    this.getBoard(this._currentBoard.getValue().id).subscribe();
  }

  getColumnIndex(columnId: string) {
    return this._currentBoard.getValue()
      .columns
      .findIndex(column => column.id === columnId);
  }

  moveColumn(columnId: string, newPosition: number) {
    const board = this._currentBoard.getValue();
    arrayMove.mutate(board.columns, this.getColumnIndex(columnId), newPosition);
    this._currentBoard.next(board)
  }

  getUsersOfBoard(boardId: string): Observable<UserBoardAccessRS[]> {
    return this.http.get<UserBoardAccessRS[]>(`/api/board/${boardId}/user`);
  }

  removeUserFromBoard(userId: string, boardId: string) {
    return this.http.delete(`/api/board/${boardId}/admin/user/${userId}`);
  }

  addUserToBoard(username: string, boardId: string): Observable<any> {
    return this.http.post(`/api/board/${boardId}/admin/user/${username}`, {});
  }

  removeBoard(boardId: string): Observable<any> {
    return this.http.delete(`/api/board/${boardId}`);
  }
}
