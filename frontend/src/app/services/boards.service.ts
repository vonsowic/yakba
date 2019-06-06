import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Board} from "../models";
import {Observable, of} from "rxjs";
import {NavigationService} from "./navigation.service";
import {catchError} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class BoardsService {

  private readonly endpoint = '/api/board';

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
    return this.http.get<Board>(this.endpoint + '/' + boardId)
      .pipe(
        catchError(err => {
          if (err.status === 404) {
            return this.navigationService.goToNotFound();
          }

          return of(err);
        })
      )
  }
}
