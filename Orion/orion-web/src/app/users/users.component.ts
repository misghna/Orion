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
    constructor(private userService: UserService) {
        
    }


  ngOnInit() {
      
  }

  activateUser(userHeader,value){
    var userId : string[] = userHeader.split("-")[1];
    console.log(userId);
    var toStatus;
    if(value=='Activate'){
      toStatus='Active';
    }else{
      toStatus="Inactive"
    }
     this.userService.changeStatus(userId,toStatus)
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

  changeRole(userHeader,value){
    var userId : string[] = userHeader.split("-")[1];
    var role;
    if(value.indexOf('Admin') >= 0){
      role='Admin';
    }else{
      role="User"
    }
     this.userService.changeRole(userId,role)
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

  transferDetail(userHeader){
    var userH : string[] = userHeader.split("-");
    this.detail = userH[0];
  }


  deleteUser(userId){
    //  this.userService.deleteUser(userId)
    // .subscribe(
    //     response => {
    //         this.data = response;     
    //     },
    //     error => {
    //       console.error(error);
    //       return {};
    //     }
    //   );
  }

  loadUserTable(){
    console.log("emit received");
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
