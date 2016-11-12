import { Component, OnInit,Output,Input,EventEmitter } from '@angular/core';
import { Http,Headers } from '@angular/http';
import { UserService } from '../service/user.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {
  @Input() loaderHidden = true;

  @Output() loadUser = new EventEmitter();
  regUser = true;
  constructor(private http:Http, private userService:UserService) {
    this.loaderHidden = true;
   }

  ngOnInit() {
      var access = JSON.parse(localStorage.getItem('accessDetail'));
      if(access!=null && access['role'] =='Admin'){
        this.regUser = false;
      }
      this.loadUserTable();
  }

 loadUserTable(){
      this.loadUser.emit({"load":"user"});
  }

    logout() {
       localStorage.setItem('accessDetail', "{}");
        this.userService.logoutUsers();
   }


}
