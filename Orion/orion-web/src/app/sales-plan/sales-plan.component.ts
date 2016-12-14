import { Component, OnInit,ElementRef} from '@angular/core';
import { SalesPlanService } from './sales-plan.service';
import { MiscService } from '../service/misc.service';
import { FilterNamePipe } from '../pipes/pipe.filterName';
import { UtilService } from '../service/util.service';


declare var jQuery : any;

@Component({
  selector: 'app-sales-plan',
  templateUrl: './sales-plan.component.html',
  styleUrls: ['./sales-plan.component.css']
})
export class SalesPlanComponent implements OnInit {
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
  bntOption = "Search";subscription;

  constructor(private salesService:SalesPlanService, private el: ElementRef,
             private miscService : MiscService,private utilService : UtilService) {

    this.subscription = utilService.currentSearchTxt$.subscribe(txt => {this.search(txt);});

    this.optionsList = [{'name':'Add New Item','value':'addNew'},
                        {'name':'Create new copy from selected plan','value':'createNewRevision'},
                        {'name':'Delete Selected Plan','value':'deleteRevision'}];
    
    this.monthNames = ["JAN", "FEB", "MAR", "APR", "MAY", "JUN","JUL", "AUG", "SEP", "OCT", "NOV", "DEC"];
    
    this.pageName;
    this.hideLoader=true;
    this.alertHidden=true;  
    this.hideAddNewForm=true;
    this.rangeSelectorHidden=true;
    this.selectedMonth = "Select Month";
    this.selectedYear = "Select Year";
    this.years = [];
    this.todayDate = this.today();


   }

  ngOnInit() {
      this.headers = [{'name':'No','value':'id','j':'x'},{'name':'RefId','value':'id','j':'l'}, {'name':'Product','value':'name','j':'l'},{'name':'Brand','value':'brand','j':'l'},
                     {'name':'Base Size', 'value':'baseSize','j':'c'}, {'name':'Base Unit','value':'baseUnit','j':'c'},
                      {'name':'Qty/pck','value':'qtyPerPack','j':'c'},{'name':'pck/cont','value': 'pckPerCont','j':'c'},
                      {'name':'Cont Size','value':'contSize','j':'c'},{'name':'Cont Qnt','value': 'contQnt','j':'c'},
                      {'name':'CIF','value':'cif','j':'c'}, {'name':'Item Origin','value':'itemOrigin','j':'c'},
                      {'name':'Dest Port','value':'destinationPort','j':'c'},{'name':'Month','value':'month','j':'c'}, {'name':'Year','value':'year','j':'c'}, 
                      {'name':'Updated On','value':'updatedOn','j':'l'}];
      
        this.loadAll(2000,'latest');
        this.getItemNameBrandList();
        this.populateYear();

        jQuery(this.el.nativeElement).find('#monthSelector li').on('click',function(){  
          jQuery('#monthBtn').html(jQuery(this).text().trim()); 
          jQuery('#monthBtn').attr('value',jQuery(this).text().trim());       
        });

        jQuery(this.el.nativeElement).on('click','#yearSelector li',function(){
          jQuery('#yearBtn').html(jQuery(this).text().trim());
          jQuery('#yearBtn').attr('value',jQuery(this).text().trim()); 
        });


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
  this.itemDetail['name'] = splited[1].split('[')[0];
  this.itemDetail['brand'] = splited[1].split('[')[1].split(']')[0];
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

  execute(year,month,task){
    if(task =="Search"){
      this.loadAll(year,month);
    }else if("Create"){
      this.createNewRevision(year,month);
    }
  }

  createNewRevision(year,month){
    var source = this.returnedRange.split(' ');
    var body = {"sourceYear":source[1], "sourceMonth" : source[0] , "newYear" : year, "newMonth":month };

    this.salesService.duplicatePlan(body)
        .subscribe(
            response => {
                this.setData(response);  
                this.popAlert("Info","success","Plan successfully duplicated!"); 
            },
            error => {
                this.popAlert("Error","danger","Something went wrong, please try again later!");
            }
          );
  }

  loadAll(year,month){
    if(!this.contains(this.monthNames,month) && month!='latest'){
      return;
    }

    if(Number(year)<2000){
      return;
    }

    var setRev :boolean;
     this.salesService.getSalesPlan(year,month)
      .subscribe(
          response => {
              this.setData(response);    
          },
          error => {
            if(error.status==404){
               this.popAlert("Error","danger","Sales plan for the selected time not found!");          
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
    console.log("returned size" + response.length);
      this.selectedMonth = response[0]['month'] ;
      this.selectedYear = response[0]['year'] ;
      this.returnedRange = response[0]['month'] + " " + response[0]['year'];
      this.responseData=response;
      this.data = response;  
  }


    addUpdateSP(event){
      event.preventDefault();
        var mon = this.monthNames.indexOf(this.itemDetail['month']);
        if(mon < 0 ){
          this.popAlert("Error","danger","Invalid month name!");
          return;
        }
        this.itemDetail['mon'] = mon;
        this.itemDetail['destinationPort'] = this.capitalizeFirstLetter(this.itemDetail['destinationPort']);
        this.itemDetail['itemOrigin'] = this.capitalizeFirstLetter(this.itemDetail['itemOrigin']);
        if (this.taskType=="Add"){
            this.itemDetail['month']
            var year = this.selectedYear == 'Select Year' ? 2000 : this.selectedYear;
            var month = this.selectedMonth == 'Select Month' ? 'latest' : this.selectedMonth;
            this.salesService.addSalesPlan(this.itemDetail,year,month)
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
          this.updateSalesPlan();
         }
    }

    parseDate(timeStamp){
      var d = new Date(timeStamp);
      var dateStr= d.getDate() + '-' + (d.getMonth()+1) + '-' + d.getFullYear();
      return dateStr;
    }

    today(){
      var today = new Date();
      var dd = today.getDate();
      var mm = today.getMonth()+1; //January is 0!
      var yyyy = today.getFullYear();
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

    delete(){
      if(this.activeProductHeader.indexOf('Plan for ')>0){
         this.deleteCurrentPlan();
         return;
      }
      var id = this.activeProductHeader.split('-')[0];
      console.log("deleting ... " + id);
      this.salesService.deleteSalesPlanById(id,this.selectedYear,this.selectedMonth)
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
      this.today();
      this.hideAddNewForm=false;
    }

    updateSalesPlan(){
        var itemList = this.allPrdList.filter(item => {
          if(item!=null){
                var splited = item.split('(')[1].split(')');
                var name = splited[1].split('[')[0];
              if(name.trim() == this.itemDetail['name'].trim()) return item;
          } 
        });

        if(itemList.length<1){
          this.popAlert("Error","danger","Bad item name, please select from the list!");
          return;
        }
            this.salesService.updateSalesPlan(this.itemDetail,this.selectedYear, this.selectedMonth)
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
        console.log("filtering ...zero length");
        this.productNameList= this.allPrdList;
        return;
      }
      console.log("filtering ..." + txt.length);
        this.productNameList = this.allPrdList.filter(item => {
          if(item!=null && item.indexOf(txt)>-1) return item;
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
      if(searchObj.searchTxt==null) return this.responseData;
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

   deleteCurrentPlan(){
     this.salesService.deleteSalesPlan(this.selectedYear,this.selectedMonth)
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
      console.log(string);
     return string.charAt(0).toUpperCase() + string.slice(1).toLowerCase();
    }

}
