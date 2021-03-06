import { Component, OnInit,ElementRef} from '@angular/core';
import { MiscService } from '../service/misc.service';
import { FilterNamePipe } from '../pipes/pipe.filterName';
import { OrdersService } from '../orders/orders.service';
import { UtilService } from '../service/util.service';
import { SalesPlanService } from '../sales-plan/sales-plan.service';
import { Router,ActivatedRoute, Params } from '@angular/router';
import { MiscSettingService } from '../misc/misc-service.service';
import { AddressBookService } from '../address-book/address-book.service';


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
  selectedDate;subscription;activeOrderId;
  salesPlanList= []; filteredSalesPlanList=[];importers;ports;

  constructor(private utilService :UtilService,private orderService:OrdersService, private el: ElementRef,
              private miscService:MiscService, private salesService:SalesPlanService,public router: Router,
              public route: ActivatedRoute, private miscSettingService :MiscSettingService,
              private addressBookService : AddressBookService) {

          this.subscription = utilService.currentSearchTxt$.subscribe(txt => {this.search(txt);});
          this.activeOrderId = this.route.snapshot.params['id'];
          this.optionsList = [{'name':'Add New Order','value':'addNew'}];
          this.utilService.setToolsContent(this.optionsList);
         
          // tools listener
          utilService.currentToolsOptCont$.subscribe(opt => { this.option(opt);}); 

          utilService.currentdelItem$.subscribe(opt => { this.delete();}); 

          this.monthNames = ["JAN", "FEB", "MAR", "APR", "MAY", "JUN","JUL", "AUG", "SEP", "OCT", "NOV", "DEC","ALL"];
          
          this.pageName;
          this.hideLoader=true;
          this.alertHidden=true;  
          this.hideAddNewForm=true;
          this.rangeSelectorHidden=true;
          this.selectedMonth = "Select Month";
          this.selectedYear = "Select Year";
          this.years = [];
          this.todayDate = this.getDate(new Date());

          router.events.subscribe((val) => {
            var newRouteParam = this.route.snapshot.params['id'];
            if(this.activeOrderId != newRouteParam){
              this.activeOrderId = newRouteParam;
                this.loadData();
            }       
          });
   }

  ngOnInit() {
      this.headers = [{'name':'No','value':'id','j':'x'},{'name':'Budget Ref Id','value':'budgetRef','j':'l'},
                      {'name':'Product','value':'item','j':'c'},{'name':'Brand','value':'brand','j':'l'},
                      {'name':'Base Size', 'value':'baseSize','j':'c'}, {'name':'Base Unit','value':'baseUnit','j':'c'},
                      {'name':'Qty/pck','value':'qtyPerPack','j':'c'},{'name':'pck/cont','value': 'pckPerCont','j':'c'},
                      {'name':'Cont Size','value':'contSize','j':'c'},{'name':'Cont Qnt','value': 'contQnt','j':'c'},
                      {'name':'Importer','value':'importer','j':'c'},{'name':'Dest Port','value':'destinationPort','j':'c'},
                      {'name':'Latest ETA','value':'latestETA','j':'c'},{'name':'Inv. No.','value':'invNo','j':'c'},
                      {'name':'OrderedBy','value':'orderedBy','j':'c'}, {'name':'Updated On','value':'updatedOn','j':'l'}];
      

        this.getItemNameBrandList();
        this.populateYear();
        this.getSalesPlan();
  //      this.getImporters();
        this.getPorts();
        this.loadData();
        this.getAddressByType("Importer");

        jQuery(this.el.nativeElement).find('#monthSelector li').on('click',function(){  
          jQuery('#monthBtn').html(jQuery(this).text().trim()); 
          jQuery('#monthBtn').attr('value',jQuery(this).text().trim());       
        });

  } 
  

    getAddressByType(type){
     this.addressBookService.get(type)
      .subscribe(
          response => {
              this.importers = response;    
          },
          error => {
            console.log("exporters not found");
          }
        );
  }


  getImporters() {
     this.miscSettingService.getImporters()
      .subscribe(
          response => {
              this.importers = response; 
          },
          error => {
               console.error("importers not found!");          
          }
        );
  } 
  
    getPorts() {
     this.miscSettingService.getPorts()
      .subscribe(
          response => {
              this.ports = response; 
          },
          error => {
               console.error("Ports not found!");          
          }
        );
  } 

loadData(){
      if(this.activeOrderId=="all"){
        this.loadAll(2000,'latest');
      }else if(this.activeOrderId=="new" || this.activeOrderId=="inTransit" || 
              this.activeOrderId=="inPort" || this.activeOrderId=="inTerminal"){
        this.getCustomOrder(this.activeOrderId);
      }else{
        this.getOrderById(this.activeOrderId);
      }
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
  this.itemDetail['itemId'] = plan.itemId;
  this.itemDetail['item'] = plan.name;
  this.itemDetail['brand'] = plan.brand;
  this.itemDetail['baseSize'] = plan['baseSize'];
  this.itemDetail['baseUnit'] = plan['baseUnit'];
  this.itemDetail['qtyPerPack'] = plan['qtyPerPack'];
  this.itemDetail['pckPerCont'] = plan['pckPerCont']; 
  this.itemDetail['contSize'] = plan['contSize'];
  this.itemDetail['contQnt'] = plan['contQnt'];
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
     this.salesService.getSalesPlan(2017,'next6')  // TBD
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
    var modalInfo = {"title" : "Order", "msg" : this.activeProductHeader.split('-')[1] + " and all related data to this order" ,"task" :"myTask"};
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

getCustomOrder(state){
       this.orderService.getAllOrders(state)
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

  getOrderById(id){
      this.orderService.getOrderById(id)
      .subscribe(
          response => {
              console.log(response);
              var resArray = [];resArray.push(response);
              this.setData(resArray);    
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
     if(response.length>0){
      var date = new Date(response[0]['createdOn']);
      this.selectedMonth = this.monthNames[date.getMonth()];
      this.selectedYear = date.getFullYear();
      this.returnedRange = this.selectedMonth+ " " + this.selectedYear;
     }
      this.responseData=response;
      this.data = response;  
  }


    addUpdateOrder(event){
      event.preventDefault();

        this.itemDetail['orderedBy'] = JSON.parse(this.utilService.getActiveUser())['fname'];
      
        if(this.itemDetail['budgetRef'] != 'none' ){
          
           var selPlan= this.salesPlanList.filter(plan => {
              if(plan.id!=null && plan.id == this.itemDetail['budgetRef'] ) return plan;
            });

            selPlan = selPlan.length>0 ? selPlan[0] : selPlan;
            if(selPlan['name']!= this.itemDetail['item']){
              this.popAlert("Error","danger","Product name doesnt much with refrenced budget item or set budget reference to none!");
              return;
            }
            if(selPlan['brand']!= this.itemDetail['brand']){
              this.popAlert("Error","danger","Brand name doesnt much with refrenced budget item or set budget reference to none!");
              return;
            }
            if(selPlan['baseUnit']!= this.itemDetail['baseUnit']){
              this.popAlert("Error","danger","Base unit doesnt much with refrenced budget item or set budget reference to none!");
              return;
            }
            if(selPlan['baseSize']!= this.itemDetail['baseSize']){
              this.popAlert("Error","danger","Base size doesnt much with refrenced budget item or set budget reference to none!");
              return;
            }
            if(selPlan['qtyPerPack']!= this.itemDetail['qtyPerPack']){
              this.popAlert("Error","danger","quantity per pack doesnt much with refrenced budget item or set budget reference to none!");
              return;
            }
            if(selPlan['itemOrigin']!= this.itemDetail['itemOrigin']){
              this.popAlert("Error","danger","Item Origin doesnt much with refrenced budget item or set budget reference to none!");
              return;
            }
            console.log(selPlan['destinationPort']);
            if(selPlan['destinationPort']!= this.itemDetail['destinationPort']){
              this.popAlert("Error","danger","Destination port doesnt much with refrenced budget item or set budget reference to none!");
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

        if(this.itemDetail['importer'] == "Select Importer"){
              this.popAlert("Error","danger","please select Importer from the list!");
              return;
        }
        this.itemDetail['destinationPort'] = this.capitalizeFirstLetter(this.itemDetail['destinationPort']);

        if (this.taskType=="Add"){
            var year = this.selectedYear == 'Select Year' ? 2000 : this.selectedYear;
            var month = this.selectedMonth == 'Select Month' ? 'latest' : this.selectedMonth;
            this.orderService.addOrder(this.itemDetail,year,month)
            .subscribe(
                response => {
                    this.popAlert("Info","success","Order successfully added!");
                    this.setData(response);  
                    this.hideAddNewForm = true;  
                },
                error => {
                  if(error.status==400){
                    this.popAlert("Error","danger",this.utilService.getErrorMsg(error));
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
      this.itemDetail['importer'] = "Select Importer";
      this.itemDetail['destinationPort'] = "Select Port";
    }

    popAlert(type,label,msg){
          this.alertHidden = false;
          this.alertType = type;
          this.alertLabel = label;
          this.itemMsg= msg;
    }

    drillDown(type,refId){
      this.router.navigate([type +  '/' + refId]);
    }

    delete(){
      var id = this.activeProductHeader.split('-')[0];
      this.orderService.deleteOrderById(id,this.selectedYear,this.selectedMonth)
      .subscribe(
          response => {
              this.setData(response);  
              this.popAlert("Info","success","Order successfully deteled!"); 
          },
          error => {
              this.setData([]);
            if(error.status==404){
               this.popAlert("Info","Info","Order for this time range not found!");          
            }else{
               this.popAlert("Error","danger","Something went wrong, please try again later!");          
            }
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
                    this.popAlert("Info","success","Order successfully updated!");
                    this.setData(response);     
                },
                error => {      
                  if(error.status==400){
                    this.popAlert("Error","danger",this.utilService.getErrorMsg(error));
                  }else{
                    this.popAlert("Error","danger","Something went wrong, please try again later!");
                  }
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
      this.data= this.utilService.search(searchObj,this.responseData,this.headers);
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

  //  deleteorder(){
  //    this.orderService.deleteOrder(this.selectedYear,this.selectedMonth)
  //     .subscribe(
  //         response => {
  //             this.setData(response);  
  //             this.popAlert("Info","success","Plan successfully deteled!"); 
  //         },
  //         error => {
  //             this.popAlert("Error","danger","Something went wrong, please try again later!");
  //         }
  //       );
  //   }

    replaceAll(string,old,newStr){
      return string.split('old').join('newStr');
    }

    capitalizeFirstLetter(string) {
     return string.charAt(0).toUpperCase() + string.slice(1).toLowerCase();
    }

}
