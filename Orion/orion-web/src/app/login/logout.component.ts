import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { Http,Headers } from '@angular/http';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LogoutComponent {
  constructor(public router: Router, public http: Http) {
  }

}
