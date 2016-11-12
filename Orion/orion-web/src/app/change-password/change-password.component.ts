import { Component, OnInit } from '@angular/core';
import { Http,Headers } from '@angular/http';
import { UserService } from '../service/user.service';

@Component({
  selector: 'app-change-password',
  templateUrl: './change-password.component.html',
  styleUrls: ['./change-password.component.css']
})
export class ChangePasswordComponent implements OnInit {
  hideLoader=true;
  changePassMsg;
  constructor(private http:Http,private userService:UserService) { }

  ngOnInit() {
  }


  changePass(event,cPass,nPass,rPass){
    event.preventDefault();

     if(nPass!=rPass){
      this.changePassMsg = "password does not match";
      return;
    }
     

    if(nPass.length<8){
      this.changePassMsg="Password must be minimum of 8 characters";
      return;
    }

    let body = JSON.stringify({"id" : this.activeUserId(), "cPass":cPass,"nPass":nPass,"rPass":rPass});
    let headerContent = new Headers();
    headerContent.append("Content-Type", "application/json");
    this.hideLoader = false;

    this.userService.changePass(body)
      .subscribe(
        response => {
              this.changePassMsg = true;
              this.changePassMsg="Password successfully changed!";
        },
        error => {
          this.changePassMsg = true;
          if(error.status==404){
            this.changePassMsg="Incorrect current password!";
          }else{
            console.log(error);
            this.changePassMsg = "Error try again later!";
          }
        }
      );
  }

    activeUserId(){
    var activeUser = JSON.parse(localStorage.getItem('accessDetail'));
    return activeUser['id'];
  }
}
