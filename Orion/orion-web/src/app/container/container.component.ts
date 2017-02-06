import { Component, OnInit,ElementRef} from '@angular/core';
import { MiscService } from '../service/misc.service';
import { FilterNamePipe } from '../pipes/pipe.filterName';
import { ContainerService } from '../container/container.service';
import { UtilService } from '../service/util.service';
import { ShippingService } from '../shipping/shipping.service';
import { ClientService } from '../client/client.service';
import {MiscSettingService} from '../misc/misc-service.service'

import { Router,ActivatedRoute, Params } from '@angular/router';

declare var jQuery : any;

@Component({
  selector: 'app-container',
  templateUrl: './container.component.html',
  styleUrls: ['./container.component.css']
})
export class ContainerComponent implements OnInit {
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
  headers = [];destinations;transporters;
  filterQuery = "";
  bntOption = "Search";
  selectedDate;activeShippingId;activeOrderId;
  activeOrder= {}; filteredSalesPlanList=[]; allOrders;shippingList; allShippingList;

  constructor(private utilService :UtilService,private contService:ContainerService, private el: ElementRef,
              private shipService:ShippingService ,public route: ActivatedRoute,public router:Router,
              private clientService :ClientService, private miscService :MiscSettingService) {
          utilService.currentSearchTxt$.subscribe(txt => {this.search(txt);});
          utilService.currentdelItem$.subscribe(opt => { this.delete();}); 
          this.optionsList = [{'name':'Add Container','value':'addNew'}];
          this.utilService.setToolsContent(this.optionsList);
              
          this.pageName;
          this.alertHidden=true;  
          this.hideAddNewForm=true;
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
            this.getAllShiping()
          }
   }

  ngOnInit() {
      this.headers = [{'name':'No','value':'id','j':'x'},{'name':'BL','value':'bl','j':'l'},
                      {'name':'Cont Size','value':'contSize','j':'c'},{'name':'Container Id','value':'contNo','j':'l'},
                      {'name':'Transporter','value':'transporter','j':'c'},{'name':'Destination','value':'destination','j':'c'},
                      {'name':'Offload Date','value':'offloadDate','j':'cd'},{'name':'Recv. Voucher No','value':'recvVoucherNo','j':'c'},
                      {'name':'Cont return date','value':'contReturnDate','j':'cd'},{'name':'Used Days','value':'totalDays','j':'c'},
                      {'name':'Remark','value':'remark','j':'c'},{'name':'Updated On','value':'updatedOn','j':'c'}];
        
        this.activeShippingId = this.route.snapshot.params['id'];
        this.loadAll(this.activeShippingId);
        
        this.getAllShiping();
        this.getDestinations();
        this.getTransporters();

        jQuery(this.el.nativeElement).find('#monthSelector li').on('click',function(){  
          jQuery('#monthBtn').html(jQuery(this).text().trim()); 
          jQuery('#monthBtn').attr('value',jQuery(this).text().trim());       
        });

        console.log(this.route.snapshot.params['id']);
  } 


getTransporters(){
     this.miscService.getProp("transporters")
      .subscribe(
          response => {
              this.transporters = response;    
          },
          error => {
            error.log("Clients/WH not found");
          }
        );
}

getDestinations(){
     this.clientService.getDestinations()
      .subscribe(
          response => {
              this.destinations = response;    
          },
          error => {
            error.log("Clients/WH not found");
          }
        );
}



updateNameBrand(nameBrand){
  var splited = nameBrand.split('(')[1].split(')');
  this.itemDetail['itemId'] = splited[0];
  this.itemDetail['item'] = splited[1].split('[')[0];
  this.itemDetail['brand'] = splited[1].split('[')[1].split(']')[0];
}

getAllShiping(){
      this.shipService.get('all')
      .subscribe(
          response => {
              this.shippingList = response; 
              this.allShippingList=response;
          },
          error => {
               console.error("order not found!");          
          }
        );
}

triggerDelModal(event){
    event.preventDefault();
    console.log("triigered");
    var modalInfo = {"title" : "Order", "msg" : "container " + this.activeProductHeader.split('-')[1],"task" :"myTask"};
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
     this.contService.get(orderId)
      .subscribe(
          response => {
              this.setData(response);    
          },
          error => {
            if(error.status==404){
               this.popAlert("Info","Info","Payment is not yet added for this order!");          
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
      this.responseData=response;
      this.data = response;  
  }


    addUpdate(event){
      event.preventDefault();
       if(this.itemDetail['contSize']=='Select Size'){
          this.popAlert("Error","danger","Please select Container Size!");
          return;
       }
        if (this.taskType=="Add"){
            this.contService.add(this.itemDetail,this.activeShippingId)
            .subscribe(
                response => {
                    this.popAlert("Info","success","Payment successfully added!");
                    this.setData(response);  
                    this.hideAddNewForm = true;  
                },
                error => {
                    if(error.status==500){
                       this.popAlert("Error","danger","Something went wrong, please try again later!");
                    }else{
                       this.popAlert("Error","danger",this.utilService.getErrorMsg(error));
                    }                   }
              );
        }else if(this.taskType=="Update"){
          this.update();
         }
    }




   filterShipByBl(bl){
        if(bl.length<1){
          return this.allOrders;
        }
        this.shippingList = this.allShippingList.filter(item => (
            (item.bl.toLowerCase().indexOf(bl.toLowerCase()) !== -1) ));
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
      this.itemDetail['contSize']='Select Size';
      this.itemDetail['transporter'] = "Select";
      this.itemDetail['destination'] = "Select";
    }

    popAlert(type,label,msg){
          this.alertHidden = false;
          this.alertType = type;
          this.alertLabel = label;
          this.itemMsg= msg;
    }

    delete(){
      var id = this.activeProductHeader.split('-')[0];
      console.log("this.activeShippingId " + this.activeShippingId);
      this.contService.deleteById(id,this.activeShippingId)
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
      console.log("id .. " + id);
      this.activeItemId = id;
      this.taskType = "Update";
      this.itemDetail = this.responseData.filter(item => item.id==id)[0];
      this.getDate(new Date());
      this.hideAddNewForm=false;
    }

    update(){
          console.log(this.itemDetail['id']);
            this.contService.update(this.itemDetail,this.activeShippingId)
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

