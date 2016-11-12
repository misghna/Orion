import { Component, OnInit } from '@angular/core';
import { Http,Headers } from '@angular/http';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {
  formHeight = 480;
  registerMsg = "";
  loaderHidden = true;
  constructor(public http: Http) {
    this.loaderHidden = true;
   }

  ngOnInit() {
  }


   register(event,fullname,email, password,rpassword) {
     event.preventDefault();
    if(password!=rpassword){
      this.registerMsg = "password does not match";
      return;
    }
    
    var name_regex =/^[a-zA-Z ]+$/;
    if(!name_regex.test(fullname)){
      this.registerMsg="Full Name : only aplha characters are allowed";
      return;
    }  

    if(password.length<8){
      this.registerMsg="Password must be minimum of 8 characters";
      return;
    }

    let body = JSON.stringify({"fullname":fullname,"email":email,"passphrase":password});
    let headerContent = new Headers();
    headerContent.append("Content-Type", "application/json");
    this.loaderHidden = false;
    this.http.post('http://localhost:8080/api/user', body, { headers: headerContent })
      .subscribe(
        response => {
          this.loaderHidden = true;
          if(response.status==200){
            this.registerMsg = "your request is pendding approval, you will get email once approved!";
          }     
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
