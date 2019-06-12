import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Board, Column, CreateCardRQ} from "../models";
import {BehaviorSubject, Observable, of} from "rxjs";
import {NavigationService} from "./navigation.service";
import {catchError} from "rxjs/operators";

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

  clearBoard() {
    this._currentBoard.next(null);
  }

  refreshBoard() {
    this.getBoard(this._currentBoard.getValue().id).subscribe();
  }
}
