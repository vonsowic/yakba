import {Component, OnInit} from '@angular/core';
import {BoardsService} from "../services/boards.service";
import {Board} from "../models";
import {NewBoardModalComponent} from "./new-board-modal/new-board-modal.component";
import {MatDialog} from "@angular/material";
import {filter, flatMap} from "rxjs/operators";

@Component({
  selector: 'app-boards',
  templateUrl: './boards.component.html',
  styleUrls: ['./boards.component.scss']
})
export class BoardsComponent implements OnInit {

  public boards: Board[] = [];
  private loading = true;

  constructor(private boardsService: BoardsService, public dialog: MatDialog) {
  }

  ngOnInit() {
    this.boardsService.getBoards()
      .subscribe(boards => {
        this.loading = false;
        this.boards = boards;
      })
  }

  public openNewBoardModal() {
    const dialogRef = this.dialog.open(NewBoardModalComponent, {
      width: '250px',
      data: {name: ''}
    });

    dialogRef.afterClosed()
      .pipe(
        filter(name => name),
        flatMap(name => this.boardsService.createNewBoard(name))
      )
      .subscribe(board => {
        this.boards.push(board);
      });
  }

}
