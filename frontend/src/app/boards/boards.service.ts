import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Board} from "../models";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class BoardsService {

  private readonly endpoint = '/api/board';

  constructor(private http: HttpClient) {
  }

  getBoards(): Observable<Board[]> {
    return this.http.get<Board[]>(this.endpoint);
  }

  createNewBoard(name: string): Observable<Board> {
    return this.http.post<Board>(this.endpoint, {
      name
    })
  }
}
