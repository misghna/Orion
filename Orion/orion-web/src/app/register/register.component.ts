import { Component, OnInit } from '@angular/core';
import { Http,Headers } from '@angular/http';
import { UserService } from '../users/users.service';
import { UtilService } from '../service/util.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {
  formHeight = 480;
  registerMsg = "";
  loaderHidden = true;

  constructor(public http: Http,private userService:UserService, private util :UtilService) {
    this.util.setHeaderState(false);  
    this.loaderHidden = true;
   }

  ngOnInit() {
  }


   register(event,fullname,email,phone, password,rpassword) {
     event.preventDefault();

    var name_regex = /\b[a-zA-Z]+\s[a-zA-Z]+\b/;
    if(!name_regex.test(fullname)){
      this.registerMsg="Invalid full name";
      return;
    }  

    var email_regex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/i;
    if(!email_regex.test(email)){
      this.registerMsg="Invalid Email Address format";
      return;
    }

    if(password!=rpassword){
      this.registerMsg = "password does not match";
      return;
    }

    var phone_regex = /\+(9[976]\d|8[987530]\d|6[987]\d|5[90]\d|42\d|3[875]\d|2[98654321]\d|9[8543210]|8[6421]|6[6543210]|5[87654321]|4[987654310]|3[9643210]|2[70]|7|1)\d{1,14}$/
    
    if(!phone_regex.test(phone)){
      this.registerMsg="Invalid Phone no";
      return;
    }  

    if(password.length<8){
      this.registerMsg="Password must be minimum of 8 characters";
      return;
    }

    let body = JSON.stringify({"fullname":fullname,"email":email,"passphrase":password,"phone":phone});
    this.loaderHidden = false;
    this.userService.signup(body)
      .subscribe(
        response => {
          this.loaderHidden = true;
            this.registerMsg = "your request is pendding approval, you will get text/email once it is approved!";  
        },
        error => {
          this.loaderHidden = true;
          if(error.status==404){
            this.registerMsg="Email address already exists";
          }else{
           this.registerMsg = "network error";
          }
        }
      );
  }


}
