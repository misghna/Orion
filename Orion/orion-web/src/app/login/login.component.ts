import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { Http,Headers } from '@angular/http';
import { UserService } from '../users/users.service';
import { UtilService } from '../service/util.service';


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
  host: {
    "[style.background-image]":"bodyBackgroundImage()" 
  }
})
export class LoginComponent {
  loaderHidden:boolean = true;
  loginMsg ="";
  constructor(public router: Router, public http: Http, private userService : UserService, private util :UtilService) {
     this.util.setHeaderState(false);  
  }

bodyBackgroundImage() {
    return 'url("http://localhost:8080/images/gridGreen.png")';
  }
  

  login(event, email, password) {
    event.preventDefault();
    var email_regex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/i;
    if(!email_regex.test(email)){
      this.loginMsg="Email Address is invalid format";
      return;
    }

      this.loaderHidden=false;
      this.userService.login(email,password)
      .subscribe(
        response => {
          this.loaderHidden=true;
            localStorage.setItem('accessDetail', JSON.stringify(response));
            localStorage.setItem('sid',"JSESSIONID=" + response['sId']);
            this.router.navigate(['']);
            this.util.setHeaderState(true);           
        },
        error => {
          this.loaderHidden=true;
          if(error.status==403){
            this.loginMsg="Account is in active, please wait for admin to activate it.";
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
