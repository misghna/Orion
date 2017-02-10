import { Component, OnInit } from '@angular/core';
import { NotificationService } from './notification.service';

@Component({
  selector: 'app-notification',
  templateUrl: './notification.component.html',
  styleUrls: ['./notification.component.css']
})
export class NotificationComponent implements OnInit {

  paymentList; documentList;statusList;deliveryMethodList;status;deliveryMethod;alertObj={};
  constructor(private notifService :NotificationService) { 
      this.alertObj['alertHidden']=true;
      this.status = ['Supplier Selected','Order Authorized','Item Shipped','Item Arrived']
      this.deliveryMethod = ['Email','SMS/Text'];

  }

  ngOnInit() {
    this.getDeliveryMethodList();
    this.getStatusList();
    this.getPaymentList();
    this.getDocumentList();
  }


  getPaymentList(){
    this.notifService.getRequired("Payment")
       .subscribe(
        response => {
                this.paymentList = this.processList('Payment',response);},
                error => { console.error("payment list not found");}
         );
  }

  getDocumentList(){
    this.notifService.getRequired("Document")
       .subscribe(
        response => {
                this.documentList = this.processList('Document',response);},
                error => { console.error("document list not found");}
         );
  }

  getStatusList(){
      this.statusList = this.processList('Status',this.status);
  }

  getDeliveryMethodList(){
      this.deliveryMethodList = this.processList('DeliveryMethod',this.deliveryMethod);
  }
  

  
  processList(type,list){
      var notifications = this.getNotifications();
      var container =[];
      if(notifications==null || notifications=='undefined'|| notifications[type] ==null){
            list.forEach(element => {
              var obj = this.setJson(element,type,false);              
                container.push(obj);
            });
      }else{     
          var selected = notifications[type];     
            list.forEach(element => {
                if ((type=='Status' || type=='DeliveryMethod') && this.contains(element,selected)){
                    var obj = this.setJson(element,type,true); 
                }else if (type!='Status' && type!='DeliveryMethod' && this.contains(element.name,selected)){
                    var obj = this.setJson(element,type,true); 
                }else{
                    var obj = this.setJson(element,type,false); 
                }             
                container.push(obj);
            });
      }
      return container;
  }


    setJson(element,type,selected){
      if(type=='Status' || type=='DeliveryMethod'){
          var obj = {'name':element,'type':type,'selected':selected};
      }else{
          var obj = {'name':element.name,'type':type,'selected':selected};
      }  
      return obj;
  }

  contains(str,arry){
    var result :boolean = false;
    arry.forEach(element => {
      if(element==str){
        result = true;
      } 
    });
    return result;
  }

  getNotifications(){
        if(localStorage.getItem('notifications')!=null || localStorage.getItem('notifications')!='undefined'){
           return JSON.parse(localStorage.getItem('notifications'));
        }
        return null;
  }

  save(){
    var payList = [];
    var docList = [];
    var statList =[];
    var mtDelList =[];

    this.deliveryMethodList.forEach(el => {
      if(el.selected==true){
        mtDelList.push(el.name);
      }
    });

    if(mtDelList.length==0){
        this.popAlert("Error","danger","you must select at least one Delivery Method !");  
        return;
    }

    this.paymentList.forEach(el => {
      if(el.selected==true){
        payList.push(el.name);
      }
    });

    this.documentList.forEach(el => {
      if(el.selected==true){
        docList.push(el.name);
      }
    });

    this.statusList.forEach(el => {
      if(el.selected==true){
        statList.push(el.name);
      }
    });


 

    var notifications = {"Payment": payList,"Document" : docList,"Status":statList,"DeliveryMethod":mtDelList};

    this.notifService.update(JSON.stringify(notifications))
    .subscribe(
    response => {
            localStorage.setItem('notifications',JSON.stringify(notifications));
            this.popAlert("Info","success","Succesfully updated!"); 
        },
            error => { 
              this.popAlert("Error","danger","Somethign went wrong, try again later!"); 
            }
      );

  }


    popAlert(type,label,msg){
          this.alertObj['alertHidden'] = false;
          this.alertObj['alertType'] = type;
          this.alertObj['alertLabel'] = label;
          this.alertObj['itemMsg']= msg;
    }

}
