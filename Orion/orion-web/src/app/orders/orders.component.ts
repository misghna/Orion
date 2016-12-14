import { Component, OnInit,ElementRef} from '@angular/core';
import { MiscService } from '../service/misc.service';
import { FilterNamePipe } from '../pipes/pipe.filterName';
import { OrdersService } from '../orders/orders.service';
import { UtilService } from '../service/util.service';
import { SalesPlanService } from '../sales-plan/sales-plan.service';
import { Router } from '@angular/router';

declare var jQuery : any;

@Component({
  selector: 'app-orders',
  templateUrl: './orders.component.html',
  styleUrls: ['./orders.component.css']
})
export class OrdersComponent implements OnInit {
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
  selectedDate;subscription;
  salesPlanList= []; filteredSalesPlanList=[];

  constructor(private utilService :UtilService,private orderService:OrdersService, private el: ElementRef,
              private miscService:MiscService, private salesService:SalesPlanService,public router: Router) {
    this.subscription = utilService.currentSearchTxt$.subscribe(txt => {this.search(txt);});

    this.optionsList = [{'name':'Add New Order','value':'addNew'}];
    
    this.monthNames = ["JAN", "FEB", "MAR", "APR", "MAY", "JUN","JUL", "AUG", "SEP", "OCT", "NOV", "DEC"];
    
    this.pageName;
    this.hideLoader=true;
    this.alertHidden=true;  
    this.hideAddNewForm=true;
    this.rangeSelectorHidden=true;
    this.selectedMonth = "Select Month";
    this.selectedYear = "Select Year";
    this.years = [];
    this.todayDate = this.getDate(new Date());
   }

  ngOnInit() {
      this.headers = [{'name':'No','value':'id','j':'x'},{'name':'Budget Ref Id','value':'budgetRef','j':'l'},
                      {'name':'Product','value':'item','j':'c'},{'name':'Brand','value':'brand','j':'l'},
                      {'name':'Base Size', 'value':'baseSize','j':'c'}, {'name':'Base Unit','value':'baseUnit','j':'c'},
                      {'name':'Qty/pck','value':'qtyPerPack','j':'c'},{'name':'pck/cont','value': 'pckPerCont','j':'c'},
                      {'name':'Cont Size','value':'contSize','j':'c'},{'name':'Cont Qnt','value': 'contQnt','j':'c'},
                      {'name':'Dest Port','value':'destinationPort','j':'c'},{'name':'Latest ETA','value':'latestETA','j':'c'},
                      {'name':'OrderedBy','value':'orderedBy','j':'c'}, {'name':'Updated On','value':'updatedOn','j':'l'}];
      
        this.loadAll(2000,'latest');
        this.getItemNameBrandList();
        this.populateYear();
        this.getSalesPlan();

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

getSalesPlan(){
    var setRev :boolean;
     this.salesService.getSalesPlan(2017,'next3')  // TBD
      .subscribe(
          response => {
              this.salesPlanList = response; 
              this.filteredSalesPlanList = response;
          },
          error => {
               console.error("budget not found!");          
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
    this.loadAll(year,month);
  }

  toggleRangeSelector(){
    this.bntOption = "Search";
    this.rangeSelectorHidden = !this.rangeSelectorHidden;
  }

  execute(task){
    if(task =="Search"){
      this.loadAll(this.selectedYear,this.selectedMonth);
    }
  }


  loadAll(year,month){
    if(!this.contains(this.monthNames,month) && month!='latest'){
      return;
    }

    if(Number(year)<2000){
      return;
    }

    var setRev :boolean;
     this.orderService.getOrder(year,month)
      .subscribe(
          response => {
              this.setData(response);    
          },
          error => {
            if(error.status==404){
               this.popAlert("Info","Info","Order for this time range not found!");          
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


    addUpdateOrder(event){
      event.preventDefault();
        if(new Date(this.itemDetail['latestETA']) < new Date() ){
          this.popAlert("Error","danger","ETA cant be less than today!");
          return;
        }
        this.itemDetail['orderedBy'] = JSON.parse(this.utilService.getActiveUser())['fname'];
      
        if(this.itemDetail['budgetRef'] != 'none' ){
          
           var selPlan= this.salesPlanList.filter(plan => {
              if(plan.id!=null && plan.id == this.itemDetail['budgetRef'] ) return plan;
            });
            selPlan = selPlan.length>0 ? selPlan[0] : selPlan;
            if(selPlan['name']!= this.itemDetail['item']){
              this.popAlert("Error","danger","Product name doesnt much with refrenced budget item!");
              return;
            }
            if(selPlan['brand']!= this.itemDetail['brand']){
              this.popAlert("Error","danger","Brand name doesnt much with refrenced budget item!");
              return;
            }
            if(selPlan['baseUnit']!= this.itemDetail['baseUnit']){
              this.popAlert("Error","danger","Base unit doesnt much with refrenced budget item!");
              return;
            }
            if(selPlan['baseSize']!= this.itemDetail['baseSize']){
              this.popAlert("Error","danger","Base size doesnt much with refrenced budget item!");
              return;
            }
            if(selPlan['qtyPerPack']!= this.itemDetail['qtyPerPack']){
              this.popAlert("Error","danger","quantity per pack doesnt much with refrenced budget item!");
              return;
            }
            if(selPlan['itemOrigin']!= this.itemDetail['itemOrigin']){
              this.popAlert("Error","danger","Item Origin doesnt much with refrenced budget item!");
              return;
            }
            if(selPlan['destinationPort']!= this.itemDetail['destinationPort']){
              this.popAlert("Error","danger","Destination port doesnt much with refrenced budget item!");
              return;
            }
        }else{
          var itemList = this.allPrdList.filter(item => {
          if(item!=null){
                    var splited = item.split('(')[1].split(')');
                    var name = splited[1].split('[')[0];
                  if(name.trim() == this.itemDetail['item'].trim()) return item;
              } 
            });

            if(itemList.length<1){
              this.popAlert("Error","danger","Bad item name, please select from the list!");
              return;
            }
        }

        this.itemDetail['destinationPort'] = this.capitalizeFirstLetter(this.itemDetail['destinationPort']);
        this.itemDetail['itemOrigin'] = this.capitalizeFirstLetter(this.itemDetail['itemOrigin']);

        if (this.taskType=="Add"){
            var year = this.selectedYear == 'Select Year' ? 2000 : this.selectedYear;
            var month = this.selectedMonth == 'Select Month' ? 'latest' : this.selectedMonth;
            this.orderService.addOrder(this.itemDetail,year,month)
            .subscribe(
                response => {
                    this.popAlert("Info","success","Item successfully added!");
                    this.setData(response);  
                    this.hideAddNewForm = true;  
                },
                error => {
                  if(error.status==400){
                    this.popAlert("Error","danger","Product Name or HSCode already exists, please check & try again!");
                  }else{
                    this.popAlert("Error","danger","Something went wrong, please try again later!");
                  }
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

    drillDown(type,refId){
      this.router.navigate(['/import/' + type +  '/' + refId]);
    }

    delete(event){
      if(this.activeProductHeader.indexOf('Plan for ')>0){
         this.deleteCurrentPlan();
         return;
      }
      var id = this.activeProductHeader.split('-')[0];
      this.orderService.deleteOrderById(id,this.selectedYear,this.selectedMonth)
      .subscribe(
          response => {
              this.setData(response);  
              this.popAlert("Info","success","Plan successfully deteled!"); 
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
            this.orderService.updateOrder(this.itemDetail,this.selectedYear, this.selectedMonth)
            .subscribe(
                response => {
                    this.hideAddNewForm = true;
                    this.popAlert("Info","success","Item successfully updated!");
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
      if(txt.length==0){
        this.filteredSalesPlanList= this.salesPlanList;
        return;
      }

        this.filteredSalesPlanList = this.salesPlanList.filter(plan => {
          if(plan.name!=null && plan.name.toLowerCase().indexOf(txt.toLowerCase())>-1 ||
             plan.brand!=null && plan.brand.toLowerCase().indexOf(txt.toLowerCase())>-1 ) return plan;
        });
    }


    getItemNameBrandList(){
      this.miscService.getItemNameBrandList()
      .subscribe(
          response => {
              this.allPrdList = response;
              this.productNameList = response;    
          },
          error => {
            this.popAlert("Error","danger","Something went wrong when load item list, please try again later!");          
          }
        );
    }


    search(searchObj){
      this.data= this.responseData.filter(order => (
        (order.item.toLowerCase().indexOf(searchObj.searchTxt.toLowerCase()) !== -1) || 
        (order.brand.toLowerCase().indexOf(searchObj.searchTxt) !== -1) || 
        (order.destinationPort.toLowerCase().indexOf(searchObj.searchTxt) !== -1)
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

   deleteCurrentPlan(){
     this.orderService.deleteOrder(this.selectedYear,this.selectedMonth)
      .subscribe(
          response => {
              this.setData(response);  
              this.popAlert("Info","success","Plan successfully deteled!"); 
          },
          error => {
              this.popAlert("Error","danger","Something went wrong, please try again later!");
          }
        );
    }

    replaceAll(string,old,newStr){
      return string.split('old').join('newStr');
    }

    capitalizeFirstLetter(string) {
     return string.charAt(0).toUpperCase() + string.slice(1).toLowerCase();
    }

}
