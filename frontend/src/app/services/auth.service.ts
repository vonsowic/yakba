import {Injectable} from '@angular/core';
import {BehaviorSubject, Observable} from "rxjs";
import {HttpClient, HttpParams} from "@angular/common/http";
import {tap} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private username = new BehaviorSubject('');

  constructor(private http: HttpClient) {
  }

  loginUser(username: string, password: string): Observable<User> {
    const body = new HttpParams()
      .set('username', username)
      .set('password', password);

    return this.http.post<User>('/api/login', body)
      .pipe(
        tap(user => {
          this.username.next(user.username);
        })
      )
  }

  fetchUsernameIfAuthenticated(): void {
    this.http.get<User>('/api/login')
      .subscribe(user => {
        this.username.next(user.username);
      })
  }

  getUsername() {
    return this.username
      .pipe(
        tap(username => {
          if (!username) {
            this.fetchUsernameIfAuthenticated()
          }
        }));
  }

}

class User {
  username: string;
}
