import { Component, OnInit } from '@angular/core';
import { StatusService } from './status.service';
import { ShippingService } from '../shipping/shipping.service';
import { OrdersService } from '../orders/orders.service';
import { Router,ActivatedRoute, Params } from '@angular/router';


declare var $ :any;

@Component({
  selector: 'app-status',
  templateUrl: './status.component.html',
  styleUrls: ['./status.component.css']
})
export class StatusComponent implements OnInit {

 payPercent; orderPercent; docPercent;statusHidden;allOrdersResponse;allOrders;activeOrder={};activeOrderId='';
 payments;documents;orders;guageLoaded;
 title = {display : true, value : 'Over all progress', fontFamily : 'Arial', fontColor : '#818181',  fontSize : 20, fontWeight : 'normal'};
 valueLabel = {display : true, fontFamily : 'Arial', fontColor : '#000', fontSize : 20, fontWeight : 'normal'};
 routeId;
  constructor(private ordersService: OrdersService,private statService :StatusService,
              public route: ActivatedRoute,public router:Router) {
    this.statusHidden=true;
      router.events.subscribe((val) => {
      var newRouteParam = this.route.snapshot.params['id'];
      this.routeId =newRouteParam;    
    });
   }

  ngOnInit() {
      this.getOrderList();
  }

  assignOrder(order){
    this.activeOrder = order;
    this.activeOrderId = '(invNo) ' + order.invNo
    if(order.bl!=null){
      this.activeOrderId = '(invNo) ' + order.invNo + " - (bl) " + order.bl; 
    }
  }

  openStat(){
      this.payments=null; this.orders=null; this.documents=null;
      this.getPaymentStat();
      this.getDocumentStat();
      this.getOrderStat();
  }

  getPaymentStat(){
    if(this.activeOrder==null){
      return;
    }  
     this.statService.getPaymentStatus(this.activeOrder['id'])
            .subscribe(
                response => {
                    this.payments =response;
                    this.payPercent = this.calcPercent(this.payments);
                    this.statusHidden = false;
                    this.overAllProgress();
                },
                error => {      
                    console.error("Something went wrong, please try again later!");
                }
      );
  }

 getDocumentStat(){

    if(this.activeOrder==null){
      return;
    }  
     this.statService.getDocStatus(this.activeOrder['id'])
            .subscribe(
                response => {
                    this.documents =response;
                    this.docPercent = this.calcPercent(this.documents);
                    this.statusHidden = false;
                    this.overAllProgress();
                },
                error => {      
                    console.error("Something went wrong, please try again later!");
                }
      );
  }

   getOrderStat(){

    if(this.activeOrder==null){
      return;
    }  
     this.statService.getOrderStatus(this.activeOrder['id'])
            .subscribe(
                response => {
                    this.orders =response;
                    this.orderPercent = this.calcPercent(this.orders);
                    this.statusHidden = false;
                    this.overAllProgress();
                },
                error => {      
                    console.error("Something went wrong, please try again later!");
                }
      );
  }

  getOrderList(){ 
     this.ordersService.getAllOrders('all')
            .subscribe(
                response => {
                    this.allOrdersResponse =response;  
                    this.allOrders =response;    
                    if(this.routeId!="search"){
                        this.activeOrder = this.getOrder(this.routeId)[0];
                        this.openStat();
                        this.assignOrder(this.activeOrder);
                    }    
                },
                error => {      
                    console.error("Something went wrong, please try again later!");
                }
      );
  }


  filterOrder(txt){
      if(txt.length==0) {
        this.allOrders = this.allOrdersResponse
      }
      this.allOrders = this.allOrdersResponse.filter(order => {
        if(order!=null && ((order.bl!=null && order.bl.toLowerCase().indexOf(txt.toLowerCase())>-1) || 
                            (order.invNo !=null &&  order.invNo.toLowerCase().indexOf(txt.toLowerCase()))>-1 ))
            return order;
      });

  }

    getOrder(id){
      return this.allOrdersResponse.filter(order => {
        if(order!=null && order.id==id)
            return order;
      });

  }


overAllProgress(){
  if(this.payments==null || this.orders==null || this.documents==null){
    return;
  }
  var mrg1 = JSON.parse(JSON.stringify(this.payments));
  Array.prototype.push.apply(mrg1,this.orders);
  Array.prototype.push.apply(mrg1,this.documents);

    var averAllProgress = this.calcPercent(mrg1);

      $('.pie_progress').asPieProgress({
        namespace: 'pie_progress'
      });
        $('.pie_progress').asPieProgress('reset');
        $('.pie_progress').asPieProgress('start');
        $('.pie_progress').asPieProgress('go',averAllProgress);


}

calcPercent(status){
  var count=0; var nrlv=0;
  status.forEach(element => {
    if(element['done']==true){
      count = count +1;
    }
  });
  return Math.round(count/(status.length)*100);
}

}



