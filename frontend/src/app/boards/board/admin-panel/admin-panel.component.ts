import {Component, OnInit} from '@angular/core';
import {BoardBaseComponent} from "../../BoardBaseComponent";
import {ActivatedRoute, Router} from "@angular/router";
import {BoardsService} from "../../../services/boards.service";
import {flatMap} from "rxjs/operators";
import {UserBoardAccessRS} from "../../../models";

@Component({
  selector: 'app-admin-panel',
  templateUrl: './admin-panel.component.html',
  styleUrls: ['./admin-panel.component.scss']
})
export class AdminPanelComponent extends BoardBaseComponent implements OnInit {

  userBoardAccesses: UserBoardAccessRS[] = [];

  newUsername = '';

  constructor(
    route: ActivatedRoute,
    private boardService: BoardsService,
    private router: Router) {
    super(route);
  }

  ngOnInit() {
    this.getBoardId()
      .pipe(flatMap(boardId => this.boardService.getUsersOfBoard(boardId)))
      .subscribe(userBoardAccesses => {
        this.userBoardAccesses = userBoardAccesses;
      })
  }

  removeUser(userId: string) {
    this.getBoardId()
      .pipe(flatMap(boardId => this.boardService.removeUserFromBoard(userId, boardId)))
      .subscribe(() => {
        this.userBoardAccesses = this.userBoardAccesses
          .filter(access => access.userId !== userId);
      })
  }

  addUser() {
    this.getBoardId()
      .pipe(flatMap(boardId => this.boardService.addUserToBoard(this.newUsername, boardId)))
      .subscribe(() => {
        const access = new UserBoardAccessRS();
        access.userId = this.newUsername;
        access.accessLevel = 'USER';
        this.userBoardAccesses.push(access);
        this.newUsername = '';
      })
  }

  removeBoard() {
    this.getBoardId()
      .pipe(flatMap(boardId => this.boardService.removeBoard(boardId)))
      .subscribe(() => {
        this.router.navigateByUrl('/')
      })
  }

  goBackToBoard() {
    this.getBoardId()
      .subscribe(boardId => {
        this.router.navigateByUrl(`/board/${boardId}`)
      })
  }
}
