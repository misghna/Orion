import { Component, OnInit,Output,EventEmitter } from '@angular/core';
import { UserService } from '../users/users.service';
import { UtilService } from '../service/util.service';
import { PaymentService } from '../payment/payment.service';
import { MiscSettingService } from '../misc/misc-service.service';



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
  userData;headerNames;allUsersResponse;approvalList =[];importers;accessList;allPaymentList;
    constructor(private userService: UserService,private utilService: UtilService,
              private payService: PaymentService,private miscService : MiscSettingService) {
        this.hideEditForm = true;
       utilService.currentSearchTxt$.subscribe(txt => {this.search(txt);});

       // this.optionsList = [{'name':'Add New Item','value':'addNew'},{'name':'Create New Revision','value':'createNewRevision'}];
       this.headerNames = [{'value':'name',"cap":"No"},{'value':'name',"cap":'Full Name'},
                           {'value':'email',"cap":'Email'},{'value': 'phone', 'cap' : 'Phone'},
                           {'value': 'department', 'cap' : 'Department'},{'value':'role','cap': 'Role'},
                           {'value':'approver','cap': 'Approver'},{'value':'privilage','cap': 'Permission'},
                           {'value':'access','cap': 'Access'},{'value':'status','cap': 'Status'},                          
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
      this.getImporters();
      this.getPayList();
  }

getImporters(){
      this.miscService.getImporters()
    .subscribe(
        response => {       
            this.importers = response;
        },
        error => {
          console.error(error);
          return {};
        }
      );
}

getPayList(){
      this.payService.getPaymentList()
    .subscribe(
        response => {       
            this.allPaymentList = response;
        },
        error => {
          console.error(error);
          return {};
        }
      );
}


    getAccessList(accessAllowedList){

      var allList = JSON.parse(JSON.stringify(this.importers));
      this.accessList =[];
      allList.forEach(el => {
            if(accessAllowedList!=null && accessAllowedList.indexOf(el)>-1){
              this.accessList.push({'type' :el, 'selected' : true});
            }else{
              this.accessList.push({'type' :el, 'selected' : false});
            }
      });
    
  }

  updateAccess(type,notSelected){
    var newAccess = [];
  //  console.log(JSON.stringify(this.accessList));
    this.accessList.forEach(el =>{
      if(el.selected || (type.trim() == el.type.trim() && !notSelected)){
        newAccess.push(el.type);
      }
    });
    this.userData['access'] =  JSON.stringify(newAccess);
  }

  getApprovalList(approved){

      var allowedList = JSON.parse(approved);
      var allList = JSON.parse(JSON.stringify(this.allPaymentList));
      allList.push({"name":"Order Authorization"});
      allList.push({"name":"Exporter Margin"});
      this.approvalList =[];
      allList.forEach(el => {
            if(approved!=null && allowedList.indexOf(el.name)>-1){
              this.approvalList.push({'type' :el.name, 'selected' : true});
            }else{
              this.approvalList.push({'type' :el.name, 'selected' : false});
            }
      });
    
  }




     search(searchObj){
      this.allUsers= this.utilService.search(searchObj,this.allUsersResponse,this.headerNames);
    }

  edit(header){ 
      var userId : string[] = header.split("-")[1];
      this.userData = this.getUser(userId)[0];
      this.hideEditForm = false;
  }


  updateApproval(){
    var newApproval = [];
    this.approvalList.forEach(el =>{
      if(el.selected){
        newApproval.push(el.type);
      }
    });
    this.userData['approver'] =  JSON.stringify(newApproval);

  }

  update(){
    this.updateApproval();
    if(Array.isArray(this.userData['access'])){
          this.userData['access'] =  JSON.stringify(this.userData['access']);
    }
    this.userService.update(this.userData)
    .subscribe(
        response => {
           this.hideEditForm=true;
           this.allUsers = response;     
        },
        error => {
          console.error(error.status);
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
