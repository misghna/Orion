import { Component, OnInit,Output,EventEmitter } from '@angular/core';
import {AgGridNg2} from 'ag-grid-ng2/main';
import {GridOptions} from 'ag-grid/main';
import { UserService } from '../service/user.service';


@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.css']
})
export class UsersComponent implements OnInit {
  private data;
  detail;
  loggedUserId;
  userIdDelete;
  hideLoader;

    constructor(private userService: UserService) {
        this.hideLoader=true;
        this.loggedUserId= this.activeUserId();
    }


  ngOnInit() {
      
  }

  activateUser(userHeader,value){
    var userId : string[] = userHeader.split("-")[1];
    var toStatus;
    if(value=='Activate'){
      toStatus='Active';
    }else{
      toStatus="Inactive"
    }
    this.hideLoader=false;
     this.userService.changeStatus(userId,toStatus)
    .subscribe(
        response => {
           this.hideLoader=true;
           this.data = response;     
        },
        error => {
          this.hideLoader=true;
          console.error(error);
          return {};
        }
      );
  }

  changeRole(userHeader,value){
    var userId : string[] = userHeader.split("-")[1];
    var role;
    if(value.indexOf('Admin') >= 0){
      role='Admin';
    }else{
      role="User"
    }
    this.hideLoader=false;
     this.userService.changeRole(userId,role)
    .subscribe(
        response => {
            this.hideLoader=true;
            this.data = response;     
        },
        error => {
          this.hideLoader=true;
          return {};
        }
      );
  }

  transferDetail(userHeader){
    var userH : string[] = userHeader.split("-");
    this.detail = userH[0];
  }

  activeUserId(){
    var activeUser = JSON.parse(localStorage.getItem('accessDetail'));
    return activeUser['id'];
  }

  storeUserId(userHeader){
    var userId : string[] = userHeader.split("-")[1];
    this.userIdDelete=userId;
  }

  clearUserId(){
    this.userIdDelete;
  }


  deleteUser(){
    this.hideLoader=false;
     this.userService.deleteUser(this.userIdDelete)
    .subscribe(
        response => {
            this.hideLoader=true;
            this.data = response;     
        },
        error => {
          this.hideLoader=true;
          return {};
        }
      );
  }

  loadUserTable(){
    this.userService.getAllUsers()
    .subscribe(
        response => {
            this.data = response;     
        },
        error => {
          console.error(error);
          return {};
        }
      );
  }

 
  private sortByWordLength = (a:any) => {
        return a.name.length;
    }


}
