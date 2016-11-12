import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { Http,Headers } from '@angular/http';
import {LoadingIndicator, LoadingPage} from '../service/loading-indicator';


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  loaderHidden:boolean = true;
  loginMsg ="";
  constructor(public router: Router, public http: Http) {
    
  }

  

  login(event, email, password) {
    event.preventDefault();
    var email_regex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/i;
    if(!email_regex.test(email)){
      this.loginMsg="Email Address is invalid format";
      return;
    }

    this.loaderHidden=false;

    let headerContent = new Headers();
    headerContent.append("Authorization", "Basic " + btoa(email + ":" + password)); 
    headerContent.append("Content-Type", "application/x-www-form-urlencoded");
    this.http.post('http://localhost:8080/api/login', null, { headers: headerContent })
      .subscribe(
        response => {
          this.loaderHidden=true;
          if(response.status==200){
            localStorage.setItem('accessDetail', JSON.stringify(response.json()));
            localStorage.setItem('sid',"JSESSIONID=" + response.json()['sId']);
          }else{
            console.log(response.json());
          }      
          
          this.router.navigate(['']);         
        },
        error => {
          this.loaderHidden=true;
          if(error.status==403){
            this.loginMsg="Account is in active, wait for admit to activate it.";
          }else{
            this.loginMsg="bad username/password";
          }
        }
        
      );
  }

  signup(event) {
    event.preventDefault();
    this.router.navigate(['signup']);
  }
}
