import {Injectable} from '@angular/core';
import {Router} from "@angular/router";

@Injectable({
  providedIn: 'root'
})
export class NavigationService {

  constructor(private router: Router) {
  }

  goToNotFound(): Promise<any> {
    return this.router.navigateByUrl('/not-found');
  }
}
