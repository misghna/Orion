import { Component, OnInit } from '@angular/core';
import { UserService } from '../service/user.service';

@Component({
  selector: 'app-passrenew',
  templateUrl: './passrenew.component.html',
  styleUrls: ['./passrenew.component.css']
})

export class PassRenewComponent implements OnInit {
  loaderHidden:boolean = true;
  newPassForm;
  reqCodeForm;
  emailId;
  verifMsg;
  changePassMsg;
  isSubmitDisabled;
  


  constructor(private userService : UserService) { 
    this.changePassMsg ="check your email for verification code!";
    this.newPassForm=true;
    this.reqCodeForm=false;
    this.isSubmitDisabled = false;
  }

  ngOnInit() {
  }


  reqCode(event,email){
    event.preventDefault();
    this.loaderHidden = false;
    this.emailId = email;
    this.userService.reqVCode(email)
      .subscribe(
        response => {
            this.loaderHidden = true;
            this.reqCodeForm=true;
            this.newPassForm=false;  
            this.isSubmitDisabled = true;
        },
        error => {
          if(error.status==404){
            this.verifMsg="User doesnt exist, check the email address & try again!";
          }else{
            console.log("error");
            console.log(error);
            this.verifMsg = "network error";
          }
        }
      );
 
  }

  changePass(event,email,vCode,password,rPassword){
    event.preventDefault();
    console.log("fired");
    if(password.length<8){
      this.changePassMsg="Password must be minimum of 8 characters";
      return;
    }

    if(password!=rPassword){
      this.changePassMsg = "password does not match";
      return;
    }

    var body = {'email':email,"vCode":vCode,"password":password};
    this.loaderHidden = false;
    this.userService.changeForgotenPass(body)
      .subscribe(
        response => {
              this.loaderHidden = true;
              this.changePassMsg="Password successfully changed!";
        },
        error => {
          this.loaderHidden = true;
          if(error.status==404){
            this.changePassMsg="check the verification code & try again!";
          }else{
            console.log(error);
            this.changePassMsg = "network error";
          }
        }
      );

  }

}
