
import { Component, OnInit,ElementRef,Renderer,ViewChild} from '@angular/core';
import { MiscService } from '../service/misc.service';
import { FilterNamePipe } from '../pipes/pipe.filterName';
import { BidService } from '../bid/bid.service';
import { UtilService } from '../service/util.service';
import { OrdersService } from '../orders/orders.service';
import { Router,ActivatedRoute, Params } from '@angular/router';
import { UserService } from '../users/users.service';
import { ApprovalService } from '../approval/approval.service';
import { CurrencyService } from '../currency/currency.service';

declare var jQuery : any;

@Component({
  selector: 'app-bid',
  templateUrl: './bid.component.html',
  styleUrls: ['./bid.component.css']
})


export class BidComponent implements OnInit {

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
  revision;
  revisionLong;
  rangeSelectorHidden;selectedMonth;selectedYear;years;returnedRange;todayDate;
  monthNames;
  productNameList=[]; allPrdList=[];
  headers = [];
  filterQuery = "";
  bntOption = "Search";
  selectedDate;activeOrderId;
  activeOrder= {}; filteredSalesPlanList=[];
  approvalAlert={};allOrders;ordersList;
  approversList;currencies;responseCurrency;
  approversPlaceHolder = "Select an Approver";

  constructor(private utilService :UtilService,private bidService:BidService, private el: ElementRef,private rd: Renderer,
              private orderService:OrdersService ,public route: ActivatedRoute,private userService : UserService,
              public router: Router,private approvalService : ApprovalService,private currencyService : CurrencyService) {
    this.approvalAlert['hidden']=true;
    this.optionsList = [{'name':'Add New Bidder','value':'addNew'}];
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
    utilService.currentToolsOptCont$.subscribe(opt => { this.option(opt);});
    utilService.currentdelItem$.subscribe(opt => { this.delete(opt);});

    router.events.subscribe((val) => {
      var newRouteParam = this.route.snapshot.params['id'];
      if(this.activeOrderId != newRouteParam){
        this.activeOrderId = newRouteParam;
          this.loadAll(this.activeOrderId);
       //   this.setTitle();
      }       
    });
   }

  ngOnInit() {
      this.headers = [{'name':'No','value':'id','j':'x'},{'name':'Inv. No','value':'invNo','j':'l'},
                      {'name':'Supplier','value':'supplier','j':'l'},{'name':'Currency','value':'currency','j':'l'},
                      {'name':'FOB','value':'fob','j':'c'},{'name':'CIF','value':'cifCnf','j':'c'},
                      {'name':'Total (CNF)', 'value':'totalBid','j':'c'}, 
                      {'name':'Payment Method','value':'paymentMethod','j':'c'},
                      {'name':'Est Transit Days','value':'estTransitDays','j':'c'},
                      {'name':'Selected','value':'selected','j':'c'},{'name':'Approval','value':'approval','j':'c'},
                      {'name':'Updated On','value':'updatedOn','j':'c'}];
      
        this.activeOrderId = this.route.snapshot.params['id'];
        this.loadAll(this.activeOrderId);
        this.populateYear();
        this.getAllOrders();
        this.getAllCurrencies();

        jQuery(this.el.nativeElement).find('#monthSelector li').on('click',function(){  
          jQuery('#monthBtn').html(jQuery(this).text().trim()); 
          jQuery('#monthBtn').attr('value',jQuery(this).text().trim());       
        });

  } 
  
populateYear() {
   var d = new Date();
   var cYear= d.getFullYear();
   for(var i = 2 ; i >-6 ; i--){
     this.years.push(cYear + i);
   }
} 


  getAllCurrencies(){

    var setRev :boolean;
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


getOrderApprovers(itemDetail){
     this.itemDetail = itemDetail;
     this.userService.getApprovers("Order Authorization")
      .subscribe(
          response => {
              this.approversList = response; 
              console.log("got response " + JSON.stringify(response));
          },
          error => {
            console.log("approvers not found");
               this.approversPlaceHolder = "Approvers not found" ;      
          }
        );
}


getAllOrders(){
      this.orderService.getAllOrders('all') 
      .subscribe(
          response => {
              this.allOrders = response; 
              this.ordersList = response;
          },
          error => {
               console.error("order not found!");          
          }
        );
}

triggerDelModal(event){
    event.preventDefault();
    console.log("triigered");
    var modalInfo = {"title" : "Order", "msg" : this.activeProductHeader.split('-')[1],"task" :"myTask"};
    this.utilService.showModalState(modalInfo);
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

  execute(task){
    if(task =="Search"){
    //  this.loadAll(this.selectedYear,this.selectedMonth);
    }
  }


  loadAll(orderId){

    var setRev :boolean;
     this.bidService.getBid(orderId)
      .subscribe(
          response => {
              this.setData(response);    
          },
          error => {
            if(error.status==404){
               this.popAlert("Info","Info","Bid is not yet added for this order!");          
            }else{
               this.popAlert("Error","danger","Something went wrong, please try again later!");          
            }
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
      var date = new Date(response[0]['createdOn']);
      this.selectedMonth = this.monthNames[date.getMonth()];
      this.selectedYear = date.getFullYear();
      this.returnedRange = this.selectedMonth+ " " + this.selectedYear;
      this.responseData=response;
      this.data = response;  
  }

  selectSupplier(event){
    event.preventDefault();
      var id = this.activeProductHeader.split('-')[0];
      this.bidService.selectBidder(id)
      .subscribe(
          response => {
              this.setData(response);  
              this.popAlert("Info","success","Bidder successfully selected!"); 
          },
          error => {
              this.popAlert("Error","danger",this.utilService.getErrorMsg(error));
          }
        );
  }

  diselectSupplier(event){
    event.preventDefault();
      var id = this.activeProductHeader.split('-')[0];
      this.bidService.diselectSupplier(id)
      .subscribe(
          response => {
              this.setData(response);  
              this.popAlert("Info","success","Winner successfully diselected!"); 
          },
          error => {
              this.popAlert("Error","danger",this.utilService.getErrorMsg(error));
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
      
      var body = {'forId':this.itemDetail['id'], 'type':'Order Authorization','forName':'Order Authorization',
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
            console.log("status "+ JSON.stringify(error));
            console.log("status code"+ error.status);
            if(JSON.stringify(error) == '{}'){
              let event = new MouseEvent('click', {bubbles: true});
              this.rd.invokeElementMethod(this.modalInput.nativeElement, 'dispatchEvent', [event]);
              this.loadAll(this.activeOrderId);
              this.approvalAlert['hidden']= true;
              this.popAlert("Info","success","Approval Request submited!"); 
            }else{
              this.popAlert("Error","danger",this.utilService.getErrorMsg(error));
              let event = new MouseEvent('click', {bubbles: true});
              this.rd.invokeElementMethod(this.modalInput.nativeElement, 'dispatchEvent', [event]);
            }
          }
        );
  }
  

    addUpdate(event){
      event.preventDefault();
        if (this.taskType=="Add"){
            this.bidService.add(this.itemDetail)
            .subscribe(
                response => {
                    this.popAlert("Info","success","Bid successfully added!");
                    this.setData(response);  
                    this.hideAddNewForm = true;  
                },
                error => {
                    this.popAlert("Error","danger","Something went wrong, please try again later!");
                }
              );
        }else if(this.taskType=="Update"){
          this.updateOrder();
         }
    }


   filterOrderByInvNo(invNo){
        if(invNo.length<1){
          return this.allOrders;
        }
        this.ordersList = this.allOrders.filter(item => (
            (item.invNo.toLowerCase().indexOf(invNo.toLowerCase()) !== -1) ));
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
      this.itemDetail['year'] = today.getFullYear();
    }

    popAlert(type,label,msg){
          this.alertHidden = false;
          this.alertType = type;
          this.alertLabel = label;
          this.itemMsg= msg;
    }

    delete(event){
      console.log("deleting ...");
      var id = this.activeProductHeader.split('-')[0];
      this.bidService.deleteById(id)
      .subscribe(
          response => {
              this.setData(response);  
              this.popAlert("Info","success","Bid successfully deteled!"); 
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
      console.log(JSON.stringify(this.itemDetail));
      this.getDate(new Date());
      this.hideAddNewForm=false;
    }

    updateOrder(){
      
            this.bidService.update(this.itemDetail)
            .subscribe(
                response => {
                    this.hideAddNewForm = true;
                    this.popAlert("Info","success","Bid successfully updated!");
                    this.setData(response);     
                },
                error => {      
                    this.popAlert("Error","danger","Something went wrong, please try again later!");
                }
              );
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



    search(searchObj){
      this.data= this.responseData.filter(item => (
        (item.name.toLowerCase().indexOf(searchObj.searchTxt.toLowerCase()) !== -1) || 
        (item.brand.toLowerCase().indexOf(searchObj.searchTxt) !== -1) || 
        (item.itemOrigin.toLowerCase().indexOf(searchObj.searchTxt) !== -1) || 
        (item.destinationPort.toLowerCase().indexOf(searchObj.searchTxt) !== -1)
        ));
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

