import { Component, OnInit,ElementRef} from '@angular/core';
import { FilterNamePipe } from '../pipes/pipe.filterName';
import { UtilService } from '../service/util.service';
import { CurrencyService } from '../currency/currency.service';
import { Router,ActivatedRoute, Params } from '@angular/router';
import { MiscSettingService } from '../misc/misc-service.service';

declare var jQuery : any;

@Component({
  selector: 'app-currency',
  templateUrl: './currency.component.html',
  styleUrls: ['./currency.component.css']
})
export class CurrencyComponent implements OnInit {
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
  miscSettings;names;currencies;responseCurrency;
  // names= ['slas','guElay','Berhe']; miscSettings;

  constructor(private utilService :UtilService,private el: ElementRef,private miscSet:MiscSettingService,
              private currencyService:CurrencyService ,public route: ActivatedRoute,public router:Router) {
          this.itemDetail['type'] = "Select Type";
          this.itemDetail['name'] = "Select Name";
          utilService.currentdelItem$.subscribe(opt => { this.delete();}); 
          this.optionsList = [{'name':'Add Currency exchange','value':'addNew'}];
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
       }

  ngOnInit() {
      this.headers = [{'name':'No','value':'id','j':'x'},{'name':'Type','value':'type','j':'l'},
                      {'name':'Name','value':'name','j':'c'},{'name':'From','value':'fromCurrency','j':'c'},
                      {'name':"To",'value':'toCurrency','j':'c'},{'name':"Rate",'value':'rate','j':'c'},
                      {'name':'Updated On','value':'updatedOn','j':'c'}];      
        this.loadAll(); 
        this.getMiscSettings();
        this.getAllCurrencies();
  } 




setType(type){
  
  this.itemDetail['type'] = type;
  if(type=='Customs'){
    this.names = ['Custom'];
  }else if(type =='Terminals' || type=='Ports'){
    this.itemDetail['name'] ='';
    this.names = this.getNamesList(type);
  }else if(type=='Other'){
    this.names = ['Other'];
  }
}


getNamesList(type){
  var cont =[];
  this.miscSettings.forEach(el => {
    if(el.name == type){
      cont = el.value.split(',');
    }
  });
  return cont;
}

triggerDelModal(event){
    event.preventDefault();
    var modalInfo = {"title" : "Order", "msg" : this.activeProductHeader.split('-')[1],"task" :"myTask"};
    this.utilService.showModalState(modalInfo);
}


  loadAll(){

    var setRev :boolean;
     this.currencyService.get()
      .subscribe(
          response => {
              this.setData(response);    
          },
          error => {
            if(error.status==404){
               this.popAlert("Info","Info","Exchange rate not yet added!");          
            }else{
               this.popAlert("Error","danger","Something went wrong, please try again later!");          
            }
          }
        );
  }

  getMiscSettings(){

    var setRev :boolean;
     this.miscSet.get()
      .subscribe(
          response => {
              this.miscSettings = response;    
          },
          error => {
            console.error(error.status);
          }
        );
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

  setData(response){
      this.responseData=response;
      this.data = response;  
  }


// filterCurrency(txt){
//   if(txt.length<1){
//       this.currencies = this.responseData
//    }
//   this.currencies = this.responseCurrency.filter(el => (
//             (el.country!=null && el.country.toLowerCase().indexOf(txt.toLowerCase()) !== -1) || 
//              (el.currency!=null && el.currency.toLowerCase().indexOf(txt.toLowerCase()) !== -1) ||
//              (el.abrevation!=null && el.abrevation.toLowerCase().indexOf(txt.toLowerCase()) !== -1)));
// }


filterCurrency(txt){
  this.currencies = this.currencyService.filterCurrency(txt,this.responseCurrency);
}

    addUpdate(event){
      event.preventDefault();
        if(this.itemDetail['type'] == "Select Type" || this.itemDetail['name'] == "Select Name"){
           this.popAlert("Error","danger","Please select Name and Type from the list!");
           return;
        }
        if (this.taskType=="Add"){
            this.currencyService.add(this.itemDetail)
            .subscribe(
                response => {
                    this.popAlert("Info","success","Exchange rate successfully added!");
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
      this.currencyService.deleteById(id)
      .subscribe(
          response => {
              this.setData(response);  
              this.popAlert("Info","success","Exchange rate successfully deteled!"); 
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

    update(){
            this.currencyService.update(this.itemDetail)
            .subscribe(
                response => {
                    this.hideAddNewForm = true;
                    this.popAlert("Info","success","Exchange rate successfully updated!");
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


