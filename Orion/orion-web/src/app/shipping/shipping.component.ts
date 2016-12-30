import { Component, OnInit,ElementRef} from '@angular/core';
import { MiscService } from '../service/misc.service';
import { FilterNamePipe } from '../pipes/pipe.filterName';
import { ShippingService } from '../shipping/shipping.service';
import { UtilService } from '../service/util.service';
import { OrdersService } from '../orders/orders.service';
import { Router,ActivatedRoute, Params } from '@angular/router';

declare var jQuery : any;

@Component({
  selector: 'app-shipping',
  templateUrl: './shipping.component.html',
  styleUrls: ['./shipping.component.css']
})
export class ShippingComponent implements OnInit {
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
  rangeSelectorHidden;selectedMonth;selectedYear;years;returnedRange;todayDate;
  monthNames;
  productNameList=[]; allPrdList=[];
  headers = [];
  filterQuery = "";
  bntOption = "Search";
  selectedDate;activeShippingId;allAgencies;
  activeOrder= {}; filteredSalesPlanList=[]; allOrders;ordersList;

  constructor(private utilService :UtilService,private shipService:ShippingService, private el: ElementRef,
              private orderService:OrdersService ,public route: ActivatedRoute,public router:Router) {
    utilService.currentdelItem$.subscribe(opt => { this.delete();}); 
    this.optionsList = [{'name':'Add Shipping','value':'addNew'}];
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
      if(this.activeShippingId != newRouteParam){
        this.activeShippingId = newRouteParam;
          this.loadAll(this.activeShippingId);
          this.setTitle();
      }       
    });

   }

   setTitle(){
          if(this.activeShippingId.indexOf("all")>=0){
            this.activeOrder = null;        
          }else{
            this.getActiveOrder()
          }
   }

  ngOnInit() {
      this.headers = [{'name':'No','value':'id','j':'x'},{'name':'Inv No','value':'invNo','j':'c'},
                      {'name':'Cont Qnt','value':'contQnt','j':'c'},{'name':'BL','value':'bl','j':'c'},
                      {'name':'Item Origin','value':'itemOrigin','j':'l'},
                      {'name':'Shipping agency','value':'shipAgency','j':'c'},{'name':'ETD','value':'etd','j':'c'},
                      {'name':'ETA', 'value':'eta','j':'c'}, {'name':'Remakr','value':'remark','j':'c'},
                      {'name':'Updated On','value':'updatedOn','j':'c'}];
      
        this.activeShippingId = this.route.snapshot.params['id'];
        this.loadAll(this.activeShippingId);
        this.populateYear();
        this.getActiveOrder();
        this.getAllShippingAgency();

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


updateNameBrand(nameBrand){
  var splited = nameBrand.split('(')[1].split(')');
  this.itemDetail['itemId'] = splited[0];
  this.itemDetail['item'] = splited[1].split('[')[0];
  this.itemDetail['brand'] = splited[1].split('[')[1].split(']')[0];
}

getActiveOrder(){
    var setRev :boolean;
    // if(this.activeShippingId=='all'){
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
    // }else{
    //  this.orderService.getOrderById(this.activeShippingId) 
    //   .subscribe(
    //       response => {
    //           this.activeOrder = response; 
    //       },
    //       error => {
    //            console.error("order not found!");          
    //       }
    //     );
    // }
}

getAllShippingAgency(){
        this.shipService.getAllAgency()
      .subscribe(
          response => {
              this.allAgencies = response; 
          },
          error => {
               console.error("order not found!");          
          }
        );
}

triggerDelModal(event){
    event.preventDefault();
    console.log("triigered");
    var modalInfo = {"title" : "Order", "msg" : 'Shipment with Bill of loading '+ this.activeProductHeader.split('-')[1],"task" :"myTask"};
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

  loadAll(orderId){

    var setRev :boolean;
     this.shipService.get(orderId)
      .subscribe(
          response => {
              this.setData(response);    
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


    addUpdate(event){
      event.preventDefault();
        if(this.itemDetail['shipAgency']=='Select Agency'){
           this.popAlert("Error","danger","Please select Agency!");
          return;
        }
        var checkInv = this.allOrders.filter(item => (
            (item.invNo.toLowerCase() == this.itemDetail['invNo'])));
            console.log("checkInv"  + checkInv);
        if(checkInv==null){
          this.popAlert("Info","success","Please select Inv No from the list!");
          return;
        }

        if (this.taskType=="Add"){
            this.shipService.add(this.itemDetail,this.activeShippingId)
            .subscribe(
                response => {
                    this.popAlert("Info","success","Shipping Info successfully added!");
                    this.setData(response);  
                    this.hideAddNewForm = true;  
                },
                error => {
                    if(error.status==500){
                       this.popAlert("Error","danger","Something went wrong, please try again later!");
                    }else{
                       this.popAlert("Error","danger",this.utilService.getErrorMsg(error));
                    }
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
      this.itemDetail['year'] = today.getFullYear();
      this.itemDetail['shipAgency'] ='Select Agency';
    }

    popAlert(type,label,msg){
          this.alertHidden = false;
          this.alertType = type;
          this.alertLabel = label;
          this.itemMsg= msg;
    }

    delete(){
      var id = this.activeProductHeader.split('-')[0];
      this.shipService.deleteById(id,this.activeShippingId)
      .subscribe(
          response => {
              this.setData(response);  
              this.popAlert("Info","success","Shipping successfully deteled!"); 
          },
          error => {
              if(error.status==500){
                  this.popAlert("Error","danger","Something went wrong, please try again later!");
              }else{
                  this.popAlert("Error","danger",this.utilService.getErrorMsg(error));
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

    update(){
            this.shipService.update(this.itemDetail,this.activeShippingId)
            .subscribe(
                response => {
                    this.hideAddNewForm = true;
                    this.popAlert("Info","success","Bid successfully updated!");
                    this.setData(response);     
                },
                error => {      
                    if(error.status==500){
                       this.popAlert("Error","danger","Something went wrong, please try again later!");
                    }else{
                       this.popAlert("Error","danger",this.utilService.getErrorMsg(error));
                    }                }
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



    filterOrderByInvNo(invNo){
        if(invNo.length<1){
          return this.allOrders;
        }
        this.ordersList = this.allOrders.filter(item => (
            (item.invNo.toLowerCase().indexOf(invNo.toLowerCase()) !== -1) ));
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

