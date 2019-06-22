import {Injectable} from '@angular/core';
import {Observable} from "rxjs";
import {HttpClient, HttpParams} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private http: HttpClient) {
  }

  loginUser(username: string, password: string): Observable<string> {
    const body = new HttpParams()
      .set('username', username)
      .set('password', password);

    return this.http.post<string>('/api/login', body)
  }
}
