import {Component, OnInit} from '@angular/core';
import {AuthService} from "../services/auth.service";
import {Router} from "@angular/router";
import {SignUpRQ} from "../models";

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.scss']
})
export class RegistrationComponent implements OnInit {

  hidePassword = true;
  signupForm = new SignUpRQ();

  constructor(private authService: AuthService, private router: Router) {
  }

  ngOnInit() {
  }

  createNewAccount() {
    this.authService.register(this.signupForm)
      .subscribe(() => {
        this.router.navigate(['/'])
      })
  }
}
