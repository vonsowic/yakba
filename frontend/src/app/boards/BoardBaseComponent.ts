import {ActivatedRoute} from "@angular/router";
import {Observable} from "rxjs";
import {map} from "rxjs/operators";
import {Component} from "@angular/core";

@Component({})
export class BoardBaseComponent {

  constructor(
    private route: ActivatedRoute
  ) {
  }

  getBoardId(): Observable<string> {
    return this.route.paramMap
      .pipe(
        map(params => params.get('boardId'))
      );
  }
}
