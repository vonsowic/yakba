import {Component, OnDestroy, OnInit} from '@angular/core';
import {BoardsService} from "../../services/boards.service";
import {ActivatedRoute} from "@angular/router";
import {flatMap, map} from "rxjs/operators";
import {Board} from "../../models";

@Component({
  selector: 'app-board',
  templateUrl: './board.component.html',
  styleUrls: ['./board.component.scss']
})
export class BoardComponent implements OnInit, OnDestroy {

  private board: Board;

  constructor(
    private route: ActivatedRoute,
    private boardService: BoardsService
  ) {
  }

  ngOnInit() {
    this.route.paramMap
      .pipe(
        map(params => params.get('boardId')),
        flatMap(boardId => this.boardService.getBoard(boardId))
      )
      .subscribe(board => {
        this.board = board;
      });
  }

  ngOnDestroy(): void {
    this.boardService.clearBoard();
  }
}
