import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Column, ColumnOrderUpdateRQ} from "../models";
import {BoardsService} from "./boards.service";
import {tap} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class ColumnService {

  constructor(private http: HttpClient, private boardService: BoardsService) {
  }

  addColumn(boardId: string, columnName: string): Observable<Column> {
    return this.http.post<Column>(`/api/board/${boardId}/column`, {name: columnName})
      .pipe(
        tap(column => {
          this.boardService.pushColumnToBoard(column);
        })
      );
  }

  deleteColumn(boardId: string, columnId: string): Observable<void> {
    return this.http.delete<void>(`/api/board/${boardId}/column/${columnId}`)
      .pipe(tap(() => {
        this.boardService.removeColumn(columnId)
      }));
  }

  moveColumnOneLeft(boardId: string, columnId: string): Observable<void> {
    const index = this.boardService.getColumnIndex(columnId);
    return this.moveColumn(boardId, columnId, index - 1);
  }

  moveColumnOneRight(boardId: string, columnId: string): Observable<void> {
    const index = this.boardService.getColumnIndex(columnId);
    return this.moveColumn(boardId, columnId, index + 1);
  }

  private moveColumn(boardId: string, columnId: string, newPosition: number): Observable<void> {
    const request = new ColumnOrderUpdateRQ();
    request.index = newPosition;

    return this.http.put<void>(`/api/board/${boardId}/column/${columnId}/order`, request)
      .pipe(
        tap(() => {
          this.boardService.moveColumn(columnId, newPosition)
        })
      )
  }
}
