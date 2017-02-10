import { Component, OnInit,ElementRef,ViewChild,Renderer} from '@angular/core';
import { MiscService } from '../service/misc.service';
import { FilterNamePipe } from '../pipes/pipe.filterName';
import { PaymentService } from '../payment/payment.service';
import { UtilService } from '../service/util.service';
import { OrdersService } from '../orders/orders.service';
import { Router,ActivatedRoute, Params } from '@angular/router';
import { UserService } from '../users/users.service';
import { ApprovalService } from '../approval/approval.service';
import { CurrencyService } from '../currency/currency.service';
import { ContainerService } from '../container/container.service';



declare var jQuery : any;

@Component({
  selector: 'app-payment',
  templateUrl: './payment.component.html',
  styleUrls: ['./payment.component.css']
})
export class PaymentComponent implements OnInit {

  @ViewChild('mdlCloseBtn') modalInput:ElementRef;


  data;responseData;itemMsg;
  alertType;alertHidden;alertLabel;
  hideAddNewForm;  hideLoader;
  itemDeleteId;
  itemDetail={};
  taskType = "Add";
  activeItemId;
  activeProductHeader;
  pageName;
  optionsList;constantOptionList;
  allOrders;allOrdersResponse;
  rangeSelectorHidden;selectedMonth;selectedYear;years;returnedRange;todayDate;
  monthNames;
  productNameList=[]; allPrdList=[];
  headers = [];
  filterQuery = "";
  bntOption = "Search";
  selectedDate;containers;
  activeOrder= {}; filteredSalesPlanList=[];allPaymentList;
  approversList;approversPlaceHolder;approvalAlert={};
  responseCurrency;currencies;

  activeOrderId;
  constructor(private utilService :UtilService,private payService:PaymentService, private el: ElementRef,
              private orderService:OrdersService ,public route: ActivatedRoute,public router : Router,
              private userService :UserService,private approvalService :ApprovalService,private rd: Renderer,
              private currencyService : CurrencyService,private contService :ContainerService) {

    this.approvalAlert['hidden']=true;
    utilService.currentSearchTxt$.subscribe(txt => {this.search(txt);});
    this.optionsList = [{'name':'Add New Payment','value':'addNew'}];
    this.utilService.setToolsContent(this.optionsList);
    
    this.monthNames = ["JAN", "FEB", "MAR", "APR", "MAY", "JUN","JUL", "AUG", "SEP", "OCT", "NOV", "DEC"];
    
    this.pageName;

    this.alertHidden=true;  
    this.hideAddNewForm=true;
    this.rangeSelectorHidden=true;
    this.selectedMonth = "Select Month";
    this.selectedYear = "Select Year";
    this.years = [];
    this.todayDate = this.getDate(new Date());

    // tools listener
    utilService.currentToolsOptCont$.subscribe(
      opt => {  
        this.option(opt);
    });

    router.events.subscribe((val) => {
      var newRouteParam = this.route.snapshot.params['id'];
      if(this.activeOrderId != newRouteParam){
        this.activeOrderId = newRouteParam;
          this.loadAll(this.activeOrderId);
          this.setTitle();
      }       
    });

   }


   getOrderApprovers(){
     this.userService.getApprovers(this.itemDetail['name'])
      .subscribe(
          response => {
              this.approversList = response; 
          },
          error => {
            console.log("approvers not found");
               this.approversPlaceHolder = "Approvers not found" ;      
          }
        );
}


 reqApproval(event){
    event.preventDefault();
    // console.log("value '" + orderRef + "'");
      if(this.itemDetail['approver']=='' || this.itemDetail['approver']==null){
        this.approvalAlert= {'hidden':false,'content':'Please select approver from list'};
        return;
      }
      var body = {'forId':this.itemDetail['id'], 'type':'Payment','forName':this.itemDetail['name'],
                  'orderRef':this.itemDetail['orderRef'],'approver':this.itemDetail['approver']};
      this.approvalService.add(body)
      .subscribe(
          response => {
              //close modal 
             let event = new MouseEvent('click', {bubbles: true});
             this.rd.invokeElementMethod(this.modalInput.nativeElement, 'dispatchEvent', [event]);
           
             // inform
              this.loadAll(this.activeOrderId);
              this.approvalAlert['hidden']= true;
              this.popAlert("Info","success","Approval Request submited!");    
          },
          error => { 
            if(error.status = 'undefined'){
              let event = new MouseEvent('click', {bubbles: true});
              this.rd.invokeElementMethod(this.modalInput.nativeElement, 'dispatchEvent', [event]);
              this.loadAll(this.activeOrderId);
              this.approvalAlert['hidden']= true;
              this.popAlert("Info","success","Approval Request submited!"); 
            }else{
              this.popAlert("Error","danger",this.utilService.getErrorMsg(error));
            }
          }
        );
  }

   setTitle(){
          if(this.activeOrderId.indexOf("all")>=0){
            this.activeOrder = null;        
          }else{
            this.getActiveOrder()
          }
   }


  ngOnInit() {
    this.activeOrderId = this.route.snapshot.params['id'];

      this.headers = [{'name':'No','value':'id','j':'x'},{'name':'Inv No','value':'invNo','j':'c'},{'name':'BL','value':'bl','j':'c'},
                      {'name':'Cont Id','value':'contId','j':'c'},{'name':'Payed For','value':'name','j':'c'},
                      {'name':'Payment Method','value':'paymentMethod','j':'l'},
                      {'name':'Currency','value':'curr','j':'c'},{'name':'Deposit', 'value':'deposit','j':'cr'},
                      {'name':'Estimated Amount', 'value':'estimate','j':'cr'},{'name':'Actual Amount', 'value':'paymentAmount','j':'cr'}, 
                      {'name':'Payment Date','value':'paymentDate','j':'cd'},{'name':'Remark','value':'remark','j':'c'},
                      {'name':'Status','value':'status','j':'c'},{'name':'Updated On','value':'updatedOn','j':'c'}];
      
        this.activeOrderId = this.route.snapshot.params['id'];
        this.loadAll(this.activeOrderId);
        this.populateYear();
        this.setTitle();
        this.getAllOrders();
        this.getAllPayList();
        this.getAllCurrencies();

        jQuery(this.el.nativeElement).find('#monthSelector li').on('click',function(){  
          jQuery('#monthBtn').html(jQuery(this).text().trim()); 
          jQuery('#monthBtn').attr('value',jQuery(this).text().trim());       
        });

  } 
  
getAllPayList(){
    this.payService.getPaymentList() 
    .subscribe(
        response => {
            this.allPaymentList = response;
        },
        error => {
              console.error("order not found!");          
        }
      );
}
populateYear() {
   var d = new Date();
   var cYear= d.getFullYear();
   for(var i = 2 ; i >-6 ; i--){
     this.years.push(cYear + i);
   }
} 


openOrder(id){
  this.router.navigate['/order/' + id];
}


updateBudgetRef(plan){
  if(plan=='none'){
    this.itemDetail['budgetRef'] = 'none';
    return;
  }
  this.itemDetail['budgetRef'] = plan.id;
  this.itemDetail['item'] = plan.name;
  this.itemDetail['brand'] = plan.brand;
  this.itemDetail['baseSize'] = plan['baseSize'];
  this.itemDetail['baseUnit'] = plan['baseUnit'];
  this.itemDetail['qtyPerPack'] = plan['qtyPerPack'];
  this.itemDetail['itemOrigin'] = plan['itemOrigin'];
  this.itemDetail['destinationPort'] = plan['destinationPort'];
  
}

updateNameBrand(nameBrand){
  var splited = nameBrand.split('(')[1].split(')');
  this.itemDetail['itemId'] = splited[0];
  this.itemDetail['item'] = splited[1].split('[')[0];
  this.itemDetail['brand'] = splited[1].split('[')[1].split(']')[0];
}

getActiveOrder(){
    var setRev :boolean;
     this.orderService.getOrderById(this.activeOrderId) 
      .subscribe(
          response => {
              this.activeOrder = response;
          },
          error => {
               console.error("order not found!");          
          }
        );
}

triggerDelModal(event){
    event.preventDefault();
    var modalInfo = {"title" : "Order", "msg" : this.activeProductHeader.split('-')[1],"task" :"myTask"};
    this.utilService.showModalState(modalInfo);
}


getContainers(orderId){
       this.contService.get(orderId)
      .subscribe(
          response => {
              this.containers = response; 
              console.log(JSON.stringify(response));
          },
          error => {
            console.error("Containers not found");
          }
        );
}
  getAllCurrencies(){
     this.currencyService.getCurrency()
      .subscribe(
          response => {
              this.currencies = response; 
              this.responseCurrency = response; 
          },
          error => {
            console.error(error.status);
          }
        );
  }

filterCurrency(txt){
  this.currencies = this.currencyService.filterCurrency(txt,this.responseCurrency);
}

updateMonthName(month){
  this.itemDetail['month'] = month;
}

updateBaseUnit(unit){
  this.itemDetail['baseUnit'] = unit;
}

  loadSelected(event,year,month){
    event.preventDefault();
  //  this.loadAll(year,month);
  }

  toggleRangeSelector(){
    this.bntOption = "Search";
    this.rangeSelectorHidden = !this.rangeSelectorHidden;
  }


  loadAll(orderId){

    var setRev :boolean;
     this.payService.get(orderId)
      .subscribe(
          response => {
              this.setData(response);    
          },
          error => {
              this.sendError(error);
          }
        );
  }

  contains(myArray,str){
    var i = myArray.length;
    while (i--) {
       if (myArray[i] === str) {
           return true;
       }
    }
    return false;
  }


  setData(response){
    if(response.length>0){
      var date = new Date(response[0]['createdOn']);
      this.selectedMonth = this.monthNames[date.getMonth()];
      this.selectedYear = date.getFullYear();
      this.returnedRange = this.selectedMonth+ " " + this.selectedYear;
    }
      this.responseData=response;
      this.data = response;  
  }


    addUpdate(event){
      event.preventDefault();
        if (this.taskType=="Add"){
            this.itemDetail['orderRef'] = this.activeOrderId;
            this.payService.add(this.itemDetail)
            .subscribe(
                response => {
                    this.popAlert("Info","success","Payment successfully added!");
                    this.setData(response);  
                    this.hideAddNewForm = true;  
                },
                error => {
                      this.sendError(error);
                }
              );
        }else if(this.taskType=="Update"){
          this.update();
         }
    }


    getDate(date){
      var dd = date.getDate();
      var mm = date.getMonth()+1; //January is 0!
      var yyyy = date.getFullYear();
      return String(dd)+'/'+ String(mm)+'/'+yyyy;
    }

    showAddForm(){
      this.taskType = "Add";
      this.hideAddNewForm=false;
      this.itemDetail={};
      var today = new Date();
      this.itemDetail['paymentDate'] = today;
      // this.itemDetail['invNo'] = this.activeOrder['invNo'];
      // this.itemDetail['bl'] = this.activeOrder['bl'];
      // this.itemDetail['orderRef'] = this.activeOrder['id'];
      // this.itemDetail['year'] = today.getFullYear();
    }


    popAlert(type,label,msg){
          this.alertHidden = false;
          this.alertType = type;
          this.alertLabel = label;
          this.itemMsg= msg;
    }

    delete(event){

      var id = this.activeProductHeader.split('-')[0];
      this.payService.deleteById(id)
      .subscribe(
          response => {
              this.setData(response);  
              this.popAlert("Info","success","Payment successfully deteled!"); 
          },
          error => {
              this.popAlert("Error","danger","Something went wrong, please try again later!");
          }
        );
    }

    editItem(idd){        
      var id = idd.split('-')[0];
      this.activeItemId = id;
      this.taskType = "Update";
      this.itemDetail = this.responseData.filter(item => item.id==id)[0];
      this.getDate(new Date());
      this.hideAddNewForm=false;
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

    update(){
            this.payService.update(this.itemDetail)
            .subscribe(
                response => {
                    this.hideAddNewForm = true;
                    this.popAlert("Info","success","Bid successfully updated!");
                    this.setData(response);     
                },
                error => {      
                    this.sendError(error);
                }
              );
    }


   sendError(error){
        if(error.status==404){
          this.popAlert("Info","Info","Payment is not yet added for this order!");  
          this.setData([]);        
        }else if(error.status==400){
          this.popAlert("Error","danger",this.utilService.getErrorMsg(error));          
        }else{
          this.popAlert("Error","danger","Something went wrong, please try again later!");   
        }
   }

    filterName(txt){
      
      if(txt.length==0){
        this.productNameList= this.allPrdList;
        return;
      }
        this.productNameList = this.allPrdList.filter(item => {
          if(item!=null && item.indexOf(txt)>-1) return item;
        });
    }

   getAllOrders(){
     this.orderService.getAllOrders('all')
            .subscribe(
                response => {
                    this.allOrdersResponse =response;  
                    this.allOrders =response;     
                },
                error => {      
                    console.error("Something went wrong, please try again later!");
                }
              );
   }

    search(searchObj){
      this.data= this.utilService.search(searchObj,this.responseData,this.headers);
    }


    option(options){
      var selected = options.optionName;
      var countDash = (selected.match(/-/g) || []).length;
      switch(true){
        case (selected == 'addNew') :
           this.showAddForm();
          break;
        default:
          console.log(selected);
      }
    }


    replaceAll(string,old,newStr){
      return string.split('old').join('newStr');
    }

    capitalizeFirstLetter(string) {
     return string.charAt(0).toUpperCase() + string.slice(1).toLowerCase();
    }

}

