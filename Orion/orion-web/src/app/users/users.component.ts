import { Component, OnInit,Output,EventEmitter } from '@angular/core';
import { UserService } from '../users/users.service';
import { UtilService } from '../service/util.service';



@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.css']
})
export class UsersComponent implements OnInit {
  private allUsers;
  detail;
  loggedUserId;
  userIdDelete;
  pageName;optionsList;
  hideEditForm;
  userData;headerNames;allUsersResponse;

    constructor(private userService: UserService,private utilService: UtilService) {
        this.hideEditForm = true;
       utilService.currentSearchTxt$.subscribe(txt => {this.search(txt);});

       // this.optionsList = [{'name':'Add New Item','value':'addNew'},{'name':'Create New Revision','value':'createNewRevision'}];
       this.headerNames = [{'value':'name',"cap":"No"},{'value':'name',"cap":'Full Name'},
                           {'value':'email',"cap":'Email'},{'value': 'phone', 'cap' : 'Phone'},
                           {'value': 'department', 'cap' : 'Department'},
                           {'value':'approver','cap': 'Approver'},
                           {'value':'role','cap': 'Role'},
                           {'value':'status','cap': 'Status'},
                           {'value':'action','cap': 'Action'}];

        this.pageName="Users";
        this.loggedUserId= this.activeUserId();
    }


  ngOnInit() {
    this.userService.getAllUsers()
    .subscribe(
        response => {       
            this.allUsersResponse = response;
            this.allUsers = response;     
        },
        error => {
          console.error(error);
          return {};
        }
      );
  }


   search(searchObj){
     if(searchObj.searchTxt==null) return this.allUsersResponse;
      this.allUsers= this.allUsersResponse.filter(user => (
        (user.fullname.toLowerCase().indexOf(searchObj.searchTxt.toLowerCase()) !== -1) || 
        (user.department.toString().indexOf(searchObj.searchTxt) !== -1)||
        (user.phone.toString().indexOf(searchObj.searchTxt) !== -1)
        ));
    }

  edit(header){ 
      var userId : string[] = header.split("-")[1];
      this.userData = this.getUser(userId)[0];
      this.hideEditForm = false;
  }

  update(){
    this.userService.update(this.userData)
    .subscribe(
        response => {
           this.hideEditForm=true;
           this.allUsers = response;     
        },
        error => {
          console.error(error);
          return {};
        }
      );
  }
  getUser(userId){
        return this.allUsers.filter(user => {
          if(user.id== userId) return user;
      });
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
     this.userService.deleteUser(this.userIdDelete)
    .subscribe(
        response => {
            this.allUsers = response;     
        },
        error => {
          return {};
        }
      );
  }

 
  private sortByWordLength = (a:any) => {
        return a.name.length;
    }


}
