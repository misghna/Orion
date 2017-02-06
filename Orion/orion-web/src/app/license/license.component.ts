import { Component, OnInit,ElementRef} from '@angular/core';
import { MiscService } from '../service/misc.service';
import { FilterNamePipe } from '../pipes/pipe.filterName';
import { LicenseService } from './license.service';
import { UtilService } from '../service/util.service';
import { OrdersService } from '../orders/orders.service';
import { Router,ActivatedRoute, Params } from '@angular/router';
import { DateDiffPipe } from '../pipes/pipe.dateDiff';

declare var jQuery : any;

@Component({
  selector: 'app-license',
  templateUrl: './license.component.html',
  styleUrls: ['./license.component.css']
})
export class LicenseComponent implements OnInit {
  data;responseData;itemMsg;
  alertType;alertHidden;alertLabel;
  hideAddNewForm;  hideLoader;
  itemDetail={};
  taskType = "Add";
  activeItemId;
  activeProductHeader;
  pageName;
  optionsList;constantOptionList;
  rangeSelectorHidden;selectedMonth;selectedYear;years;returnedRange;todayDate;
  monthNames;
  productNameList=[]; allPrdList=[];
  headers = [];activeDocId='';
  filterQuery = "";
  bntOption = "Search";
  selectedDate;docType;
  activeOrder= {}; filteredSalesPlanList=[];
  allOrdersResponse;allOrders;

  constructor(private utilService :UtilService,private licenseService:LicenseService, private el: ElementRef,
              public route: ActivatedRoute,public router: Router,private orderService:OrdersService) {

    this.optionsList = [{'name':'Add New License','value':'addNew'}];
    this.utilService.setToolsContent(this.optionsList);
        
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
      opt => {this.option(opt);});

    utilService.currentdelItem$.subscribe(
      opt => {this.delete(opt);});

    utilService.currentDocList$.subscribe(
      docs => {this.setData(docs);});

    router.events.subscribe((val) => {
      var newRouteParam = this.route.snapshot.params['id'];
      if(this.activeDocId != newRouteParam){
        this.activeDocId = newRouteParam;
          this.loadAll(this.activeDocId);
          if(this.activeDocId.indexOf("all")<0){
            this.getActiveOrder()
          }else{
            this.activeOrder = null;
            this.docType = this.activeDocId.replace(/\_/g,' ')
          }
      }
        
    });

   }
//{'name':'Courier','value':'courier','j':'c'},{'name':'Tracking Id','value':'trackingId','j':'c'}
  ngOnInit() {
      this.headers = [{'name':'No','value':'id','j':'x'},
                      {'name':'Order Invoice No','value':'invNo','j':'c'},{'name':'Bill No','value':'bl','j':'c'},
                      {'name':'Pedidu No','value':'pediduNo','j':'l'},{'name':'DU No','value':'duNo','j':'l'},
                      {'name':'GIT','value':'git','j':'l'},{'name':'Issue Date','value':'issueDate','j':'cd'},
                      {'name':'Expire Date','value':'expireDate','j':'cd'}];

        var activeOrderId = this.route.snapshot.params['id'];
        this.getAllOrders();
        // if(this.activeDocId.indexOf("orderRef")>=0){
        //   this.getActiveOrder()
        // }else{
        //   this.activeOrder = null;
        //   this.docType = this.activeDocId.replace(/\_/g,' ')
        // }
        this.loadAll(this.activeDocId);
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

getActiveOrder(){
    if(this.activeDocId==null || this.activeDocId=='')return;
     this.orderService.getOrderById(this.activeDocId.split("-")[1]) 
      .subscribe(
          response => { 
              this.activeOrder = response; 
          },
          error => {
               console.error("order not found!");          
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


triggerDelModal(event){
    event.preventDefault();
    console.log("triigered");
    var modalInfo = {"title" : "Order", "msg" : this.activeProductHeader.split('-')[1],"task" :"myTask"};
    this.utilService.showModalState(modalInfo);
}

  loadAll(orderId){
    if(orderId==null || orderId=='')return;
     this.licenseService.get(orderId)
      .subscribe(
          response => {
              this.setData(response);    
          },
          error => {           
            if(error.status==404){
               this.setData([]);
               this.popAlert("Info","Info","Document is not yet added for this order!");          
            }else{
               this.popAlert("Error","danger","Something went wrong, please try again later!");          
            }
          }
        );
  }


  openItem(id){

  }

  setData(response){
      this.responseData=response;
      this.data = response;  
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
      console.log(this.activeDocId + "---"+ id);
      this.licenseService.deleteById(this.activeDocId,id)
      .subscribe(
          response => {
              this.setData(response);  
              this.popAlert("Info","success","License successfully deteled!"); 
          },
          error => {
            if(error.status==404){
               this.setData([]);
               this.popAlert("Info","Info","License is not yet added for this order!");          
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


 addUpdate(event){
      event.preventDefault();
        if (this.taskType=="Add"){
            this.licenseService.add(this.itemDetail,this.activeDocId)
            .subscribe(
                response => {
                    this.popAlert("Info","success","Payment successfully added!");
                    this.setData(response);  
                    this.hideAddNewForm = true;  
                },
                error => {
                    if(error.status!=500){
                       this.popAlert("Error","danger",this.utilService.getErrorMsg(error));
                    }else{
                       this.popAlert("Error","danger","Something went wrong, please try again later!");
                    }
                }
              );
        }else if(this.taskType=="Update"){
          this.update();
         }
    }


    update(){
            this.licenseService.update(this.itemDetail,this.activeDocId)
            .subscribe(
                response => {
                    this.hideAddNewForm = true;
                    this.popAlert("Info","success","License successfully updated!");
                    this.setData(response);     
                },
                error => {      
                    if(error.status!=500){
                       this.popAlert("Error","danger",this.utilService.getErrorMsg(error));
                    }else{
                       this.popAlert("Error","danger","Something went wrong, please try again later!");
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
           console.log(selected);
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

