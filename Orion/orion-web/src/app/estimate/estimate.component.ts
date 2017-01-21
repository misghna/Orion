import { Component, OnInit } from '@angular/core';
import { OrdersService } from '../orders/orders.service';
import { Router,ActivatedRoute, Params } from '@angular/router';
import { EstimateService } from './estimate.service';
import { UtilService } from '../service/util.service';


@Component({
  selector: 'app-estimate',
  templateUrl: './estimate.component.html',
  styleUrls: ['./estimate.component.css']
})
export class EstimateComponent implements OnInit {

  allOrdersResponse;allOrders;routeId;activeOrder;activeOrderId;estimates;parsedResult=[];
  statusHidden;alertObj={};

  constructor(private ordersService:OrdersService,public route: ActivatedRoute,public router:Router,
  private estimateService:EstimateService,private utilService:UtilService ) { 
  this.statusHidden = true;
   router.events.subscribe((val) => {
      var newRouteParam = this.route.snapshot.params['id'];
      this.routeId =newRouteParam;    
    });
}

  ngOnInit() {
      this.getOrderList();
      this.alertObj['alertHidden'] = true;
  }



  getEstimate(){ 
     this.estimateService.getEstimate(this.activeOrder.id)
            .subscribe(
                response => {
                    this.estimates = response;
                    var j = response['details'];
                    var mainKeys = Object.keys(j);
                    this.parsedResult =[];
                    mainKeys.forEach(el => {
                      var keys = Object.keys(j[el]);
                      var total =  j[el]['Total'];
                      var index = keys.indexOf('Total');
                      keys.splice(index, 1);
                       this.parsedResult.push({'name':el,'keys' : keys,'total' : total});
                    });
                    this.statusHidden=false;
                     this.alertObj['alertHidden'] = true;
                },
                error => {      
                    if(error.status==404){
                    this.popAlert("Info","Info","Shipping Details is not yet added for this order!");          
                    }else if(error.status==400){
                    this.popAlert("Error","danger",this.utilService.getErrorMsg(error));
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

  getOrderList(){ 
     this.ordersService.getAllOrders('all')
            .subscribe(
                response => {
                    this.allOrdersResponse =response;  
                    this.allOrders =response;    
                    if(this.routeId!="search"){
                        this.activeOrder = this.getOrder(this.routeId)[0];
                        this.assignOrder(this.activeOrder);
                    }    
                },
                error => {      
                    console.error("Something went wrong, please try again later!");
                }
      );
  }


getOrder(id){
      return this.allOrdersResponse.filter(order => {
        if(order!=null && order.id==id)
            return order;
      });
  }

    assignOrder(order){
    this.activeOrder = order;
    this.activeOrderId = '(invNo) ' + order.invNo
    if(order.bl!=null){
      this.activeOrderId = '(invNo) ' + order.invNo + " - (bl) " + order.bl; 
    }
  }

}
