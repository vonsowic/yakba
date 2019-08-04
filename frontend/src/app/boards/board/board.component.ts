import {Component, OnDestroy, OnInit} from '@angular/core';
import {BoardsService} from "../../services/boards.service";
import {ActivatedRoute, Router} from "@angular/router";
import {flatMap} from "rxjs/operators";
import {Board} from "../../models";
import {BoardBaseComponent} from "../BoardBaseComponent";
import {CardService} from "../../services/card.service";

@Component({
  selector: 'app-board',
  templateUrl: './board.component.html',
  styleUrls: ['./board.component.scss']
})
export class BoardComponent extends BoardBaseComponent implements OnInit, OnDestroy {

  board: Board;
  displayCardDrawer = false;

  constructor(
    route: ActivatedRoute,
    private router: Router,
    private boardService: BoardsService,
    private cardService: CardService,
  ) {
    super(route)
  }

  ngOnInit() {
    this.getBoardId()
      .pipe(
        flatMap(boardId => this.boardService.getBoard(boardId))
      )
      .subscribe(board => {
        this.board = board;
      });

    this.cardDrawerOpenerListener();
  }

  ngOnDestroy(): void {
    this.boardService.clearBoard();
  }

  cardDrawerOpenerListener() {
    this.cardService.getSelectedCard()
      .subscribe(card => {
        this.displayCardDrawer = card.id != '';
      })
  }

  goToSettings() {
    this.getBoardId()
      .subscribe(boardId => this.router.navigateByUrl(`/board/${boardId}/settings`));
  }
}
