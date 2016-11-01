import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { Http,Headers } from '@angular/http';
//import { contentHeaders } from '../common/headers';


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  constructor(public router: Router, public http: Http) {
  }

  login(event, username, password) {
    event.preventDefault();
    let body = JSON.stringify({ username, password });
//    let body = JSON.stringify({ "login": "web" });
    let headerContent = new Headers();
    console.log(username + ":" + password)
    headerContent.append("Authorization", "Basic " + btoa(username + ":" + password)); 
    headerContent.append("Content-Type", "application/x-www-form-urlencoded");
    this.http.post('http://localhost:8080/api/login', body, { headers: headerContent })
      .subscribe(
        response => {
          if(response.status==200){
            localStorage.setItem('access', "granted");
          }
          console.log(response.status)
          this.router.navigate(['']);
        },
        error => {
          alert(error.text());
          console.log(error.text());
        }
      );
  }

  signup(event) {
    event.preventDefault();
    this.router.navigate(['signup']);
  }
}
