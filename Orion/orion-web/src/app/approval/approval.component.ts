import { Component, OnInit } from '@angular/core';
import { Router,ActivatedRoute, Params } from '@angular/router';
import { ApprovalService } from '../approval/approval.service';
import { UtilService } from '../service/util.service';


@Component({
  selector: 'app-approval',
  templateUrl: './approval.component.html',
  styleUrls: ['./approval.component.css']
})
export class ApprovalComponent implements OnInit {
  
  activeOrderId;responseData;data;alertObj={};headers;activeApproval;

  constructor(public route: ActivatedRoute,private appovalService :ApprovalService, public router :Router, private utilService :UtilService) {
          this.headers = [{'name':'No','value':'id','j':'x'},{'name':'Inv No','value':'invNo','j':'c'},
                      {'name':'BL','value':'bl','j':'c'},{'name':'Approval Type','value':'type','j':'l'},{'name':'For','value':'forName','j':'l'},
                      {'name':'Requested By','value':'requestedBy','j':'c'},{'name':'Requested On','value':'requestedOn','j':'c'},
                      {'name':'Approver', 'value':'approver','j':'c'},
                      {'name':'Status','value':'status','j':'c'}, {'name':'Approved On','value':'approvedOn','j':'c'}];
         
          this.alertObj['alertHidden']=true;

          router.events.subscribe((val) => {
            var newRouteParam = this.route.snapshot.params['id'];
            if(this.activeOrderId != newRouteParam){
              this.activeOrderId = newRouteParam;
              this.loadAll(newRouteParam);
            }       
          });
   }

  ngOnInit() {
        this.activeOrderId = this.route.snapshot.params['id'];
        this.loadAll(this.activeOrderId);
  }



  loadAll(orderId){

    var setRev :boolean;
     this.appovalService.get(orderId)
      .subscribe(
          response => {
              this.responseData=response;
              this.data = response;     
          },
          error => {
            if(error.status==404){
               this.popAlert("Info","Info","Shipping Details is not yet added for this order!");          
            }else{
               this.popAlert("Error","danger","Something went wrong, please try again later!");          
            }
          }
        );
  }


    popAlert(type,label,msg){
          this.alertObj['alertHidden'] = false;
          this.alertObj['alertType'] = type;
          this.alertObj['alertLabel'] = label;
          this.alertObj['itemMsg']= msg;
    }


approve(){

     this.appovalService.approve(this.activeApproval)
      .subscribe(
          response => {
              this.responseData=response;
              this.data = response;     
          },
          error => {
             if(error.status!=500){
               console.log(error.status);
               this.popAlert("Error","danger",this.utilService.getErrorMsg(error));          
            }else{
               this.popAlert("Error","danger","Something went wrong, please try again later!");          
            }
          }
        );

}


}
