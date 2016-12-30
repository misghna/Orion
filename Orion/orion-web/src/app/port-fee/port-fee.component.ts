import { Component, OnInit,ElementRef} from '@angular/core';
import { MiscService } from '../service/misc.service';
import { FilterNamePipe } from '../pipes/pipe.filterName';
import { ContainerService } from '../container/container.service';
import { UtilService } from '../service/util.service';
import { PortFeeService } from '../port-fee/port-fee.service';
import { Router,ActivatedRoute, Params } from '@angular/router';

declare var jQuery : any;

@Component({
  selector: 'app-port-fee',
  templateUrl: './port-fee.component.html',
  styleUrls: ['./port-fee.component.css']
})
export class PortFeeComponent implements OnInit {
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
  todayDate;
  monthNames;
  productNameList=[]; allPrdList=[];
  headers = [];
  filterQuery = "";
  bntOption = "Search";
  selectedDate;activeShippingId;activeOrderId;
  activeOrder= {}; filteredSalesPlanList=[]; allOrders;shippingList; allShippingList;

  constructor(private utilService :UtilService,private el: ElementRef,
              private portFeeService:PortFeeService ,public route: ActivatedRoute,public router:Router) {
          
          utilService.currentdelItem$.subscribe(opt => { this.delete();}); 
          this.optionsList = [{'name':'Add Agency','value':'addNew'}];
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

          // router.events.subscribe((val) => {
          //   var newRouteParam = this.route.snapshot.params['id'];
          //   if(this.activeShippingId != newRouteParam){
          //     this.activeShippingId = newRouteParam;
          //       this.loadAll(this.activeShippingId);
          //       this.setTitle();
          //   }       
          // });

   }

  //  setTitle(){
  //         if(this.activeShippingId.indexOf("all")>=0){
  //           this.activeOrder = null;        
  //         }else{
  //           this.getAllShiping()
  //         }
  //  }

  ngOnInit() {
      this.headers = [{'name':'No','value':'id','j':'x'},{'name':'Agency','value':'agency','j':'l'},
                      {'name':'Legalization Fee','value':'legalizationFee','j':'c'},{'name':'Cont Lift Fee','value':'contLiftFee','j':'c'},
                      {'name':"Deposit 20'",'value':'depositCont20','j':'c'},{'name':"Deposit 40'",'value':'depositCont40','j':'c'},
                      {'name':"Consumer Tax'",'value':'consumerTax','j':'c'},{'name':"Cont Free Days'",'value':'contFreeDays','j':'c'},
                      {'name':'Dayly Penality Rate','value':'dailyPenality','j':'c'},{'name':'Updated On','value':'updatedOn','j':'c'}];
        
        this.activeShippingId = this.route.snapshot.params['id'];
        this.loadAll();
        
   //     this.getAllShiping();

        jQuery(this.el.nativeElement).find('#monthSelector li').on('click',function(){  
          jQuery('#monthBtn').html(jQuery(this).text().trim()); 
          jQuery('#monthBtn').attr('value',jQuery(this).text().trim());       
        });

        console.log(this.route.snapshot.params['id']);
  } 



updateNameBrand(nameBrand){
  var splited = nameBrand.split('(')[1].split(')');
  this.itemDetail['itemId'] = splited[0];
  this.itemDetail['item'] = splited[1].split('[')[0];
  this.itemDetail['brand'] = splited[1].split('[')[1].split(']')[0];
}

// getAllShiping(){
//       this.portFeeService.get()
//       .subscribe(
//           response => {
//               this.shippingList = response; 
//               this.allShippingList=response;
//           },
//           error => {
//                console.error("order not found!");          
//           }
//         );
// }

triggerDelModal(event){
    event.preventDefault();
    console.log("triigered");
    var modalInfo = {"title" : "Order", "msg" : "Agency " + this.activeProductHeader.split('-')[1],"task" :"myTask"};
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


  loadAll(){

    var setRev :boolean;
     this.portFeeService.get()
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
        if (this.taskType=="Add"){
            this.portFeeService.add(this.itemDetail)
            .subscribe(
                response => {
                    this.popAlert("Info","success","Shipping Agency successfully added!");
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
    }

    popAlert(type,label,msg){
          this.alertHidden = false;
          this.alertType = type;
          this.alertLabel = label;
          this.itemMsg= msg;
    }

    delete(){
      var id = this.activeProductHeader.split('-')[0];
      this.portFeeService.deleteById(id)
      .subscribe(
          response => {
              this.setData(response);  
              this.popAlert("Info","success","Shipping Agency successfully deteled!"); 
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
            this.portFeeService.update(this.itemDetail)
            .subscribe(
                response => {
                    this.hideAddNewForm = true;
                    this.popAlert("Info","success","Shipping Agency successfully updated!");
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


