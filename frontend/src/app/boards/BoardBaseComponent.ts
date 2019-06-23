import {ActivatedRoute} from "@angular/router";
import {Observable} from "rxjs";
import {map} from "rxjs/operators";

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
