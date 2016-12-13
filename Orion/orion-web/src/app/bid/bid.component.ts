
import { Component, OnInit,ElementRef} from '@angular/core';
import { MiscService } from '../service/misc.service';
import { FilterNamePipe } from '../pipes/pipe.filterName';
import { BidService } from '../bid/bid.service';
import { UtilService } from '../service/util.service';
import { OrdersService } from '../orders/orders.service';
import { Router,ActivatedRoute, Params } from '@angular/router';

declare var jQuery : any;

@Component({
  selector: 'app-bid',
  templateUrl: './bid.component.html',
  styleUrls: ['./bid.component.css']
})
export class BidComponent implements OnInit {
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

  constructor(private utilService :UtilService,private bidService:BidService, private el: ElementRef,
              private orderService:OrdersService ,public route: ActivatedRoute) {

    this.optionsList = [{'name':'Add New Bidder','value':'addNew'}];
    
    this.monthNames = ["JAN", "FEB", "MAR", "APR", "MAY", "JUN","JUL", "AUG", "SEP", "OCT", "NOV", "DEC"];
    
    this.pageName;

    this.alertHidden=true;  
    this.hideAddNewForm=true;
    this.rangeSelectorHidden=true;
    this.selectedMonth = "Select Month";
    this.selectedYear = "Select Year";
    this.years = [];
    this.todayDate = this.getDate(new Date());
   }

  ngOnInit() {
      this.headers = [{'name':'No','value':'id','j':'x'},{'name':'Supplier','value':'supplier','j':'l'},
                      {'name':'FOB','value':'fob','j':'c'},{'name':'CIF/CNF','value':'cifCnf','j':'c'},
                      {'name':'Total Bid Amount', 'value':'totalBid','j':'c'}, 
                      {'name':'Payment Method','value':'paymentMethod','j':'c'},
                      {'name':'Selected','value':'selected','j':'c'},{'name':'Approval','value':'approval','j':'c'},
                      {'name':'Updated On','value':'updatedOn','j':'c'}];
      
        this.activeOrderId = this.route.snapshot.params['id'];
        this.loadAll(this.activeOrderId);
        this.getItemNameBrandList();
        this.populateYear();
        this.getActiveOrder();

        jQuery(this.el.nativeElement).find('#monthSelector li').on('click',function(){  
          jQuery('#monthBtn').html(jQuery(this).text().trim()); 
          jQuery('#monthBtn').attr('value',jQuery(this).text().trim());       
        });

        console.log(this.route.snapshot.params['id']);
  } 
  
populateYear() {
   var d = new Date();
   var cYear= d.getFullYear();
   for(var i = 2 ; i >-6 ; i--){
     this.years.push(cYear + i);
   }
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
    }else if("Create"){
      this.createNewRevision(this.selectedYear,this.selectedMonth);
    }
  }

  createNewRevision(year,month){
    var source = this.returnedRange.split(' ');
    var body = {"sourceYear":source[1], "sourceMonth" : source[0] , "newYear" : year, "newMonth":month };

    // this.bidService.duplicatePlan(body)
    //     .subscribe(
    //         response => {
    //             this.setData(response);  
    //             this.popAlert("Info","success","Plan successfully duplicated!"); 
    //         },
    //         error => {
    //             this.popAlert("Error","danger","Something went wrong, please try again later!");
    //         }
    //       );
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
      var id = this.activeProductHeader.split('-')[0];
      this.bidService.reqApproval(id)
      .subscribe(
          response => {
              this.setData(response);  
              this.popAlert("Info","success","Approval Request is sent to all Approvers!"); 
          },
          error => {
              this.popAlert("Error","danger",this.utilService.getErrorMsg(error));
          }
        );
  }
  

    addUpdate(event){
      event.preventDefault();
      this.itemDetail['orderRef']=this.activeOrderId;
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

    parseDate(timeStamp){
      var d = new Date(timeStamp);
      var dateStr= d.getDate() + '-' + (d.getMonth()+1) + '-' + d.getFullYear();
      return dateStr;
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

  filterBudget(txt){
      // if(txt.length==0){
      //   this.filteredSalesPlanList= this.salesPlanList;
      //   return;
      // }

        // this.filteredSalesPlanList = this.salesPlanList.filter(plan => {
        //   if(plan.name!=null && plan.name.toLowerCase().indexOf(txt.toLowerCase())>-1 ||
        //      plan.brand!=null && plan.brand.toLowerCase().indexOf(txt.toLowerCase())>-1 ) return plan;
        // });
    }


    getItemNameBrandList(){
    //   this.miscService.getItemNameBrandList()
    //   .subscribe(
    //       response => {
    //           this.allPrdList = response;
    //           this.productNameList = response;    
    //       },
    //       error => {
    //         this.popAlert("Error","danger","Something went wrong when load item list, please try again later!");          
    //       }
    //     );
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
        case (selected == 'createNewRevision') :
            this.bntOption = "Create";
            this.rangeSelectorHidden = false;
           // this.createNewRevision(this.revision);
          break;
        case (selected == 'deleteRevision') :
           this.activeProductHeader = 'Sales Plan for ' +  this.selectedMonth + '/' + this.selectedYear;
           jQuery('#itemModal').modal('show');
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

