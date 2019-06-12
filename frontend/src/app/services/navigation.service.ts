import {Injectable} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {Observable} from "rxjs";
import {map} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class NavigationService {

  constructor(private route: ActivatedRoute,
              private router: Router) {
  }

  goToNotFound(): Promise<any> {
    return this.router.navigateByUrl('/not-found');
  }

  getCurrentBoardId(): Observable<string> {
    return this.route.params
      .pipe(
        map(params => params['boardId'])
      )
  }
}
