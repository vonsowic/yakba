import {Injectable} from '@angular/core';
import {BehaviorSubject, Observable} from "rxjs";
import {HttpClient, HttpParams} from "@angular/common/http";
import {flatMap, tap} from "rxjs/operators";
import {SignUpRQ} from "../models";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private username = new BehaviorSubject('');

  constructor(private http: HttpClient) {
  }

  loginUser(username: string, password: string): Observable<void> {
    const body = new HttpParams()
      .set('username', username)
      .set('password', password);

    return this.http.post<void>('/api/login', body)
      .pipe(
        tap(() => {
          this.fetchUsernameIfAuthenticated()
        })
      )
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

  fetchUsernameIfAuthenticated(): void {
    this.http.get<User>('/api/user')
      .subscribe(user => {
        this.username.next(user.username);
      })
  }

  logout(): Observable<any> {
    return this.http.post('/api/logout', {})
  }

  register(req: SignUpRQ): Observable<void> {
    return this.http.post<void>('/api/user', req)
      .pipe(
        flatMap(() => this.loginUser(req.username, req.password))
      )
  }
}

class User {
  username: string;
}
