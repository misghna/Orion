import { Component, OnInit,ElementRef} from '@angular/core';
import { BudgetService } from './budget.service';
import { MiscService } from '../service/misc.service';
import { FilterNamePipe } from '../pipes/pipe.filterName';
import { UtilService } from '../service/util.service';
import { MiscSettingService } from '../misc/misc-service.service';


declare var jQuery : any;

@Component({
  selector: 'app-budget',
  templateUrl: './budget.component.html',
  styleUrls: ['./budget.component.css']
})
export class BudgetComponent implements OnInit {
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
  monthNames; totalItems;totalFees=0;totalSalesEst=0;
  productNameList=[]; allPrdList=[];
  headers = [];
  filterQuery = "";
  bntOption = "Search";subscription;ports;

  constructor(private budgetService:BudgetService, private el: ElementRef,
             private miscService : MiscService,private utilService : UtilService,private miscSettingService: MiscSettingService) {
    utilService.currentSearchTxt$.subscribe(txt => {this.search(txt);});
    this.subscription = utilService.currentSearchTxt$.subscribe(txt => {this.search(txt);});

    this.optionsList = [{'name':'Recalculate Budget','value':'addNew'}];
    this.utilService.setToolsContent(this.optionsList);

    
    // tools listener
    utilService.currentToolsOptCont$.subscribe(
      opt => { 
        this.option(opt);
    });    
    
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
      this.headers = [{'name':'No','value':'id','j':'x'},{'name':'RefId','value':'id','j':'l'}, {'name':'Product','value':'product','j':'l'},{'name':'Brand','value':'brand','j':'l'},
                     {'name':'Base Size', 'value':'baseSize','j':'c'}, {'name':'Base Unit','value':'baseUnit','j':'c'},
                      {'name':'Qty/pck','value':'qtyPerPack','j':'c'},{'name':'pck/cont','value': 'pckPerCont','j':'c'},
                      {'name':'Cont Size','value':'contSize','j':'c'},{'name':'Cont Qnt','value': 'contQnt','j':'c'},
                      {'name':'CIF($)','value':'cif','j':'c'},{'name':'Total CIF($)','value':'totalCif','j':'cr'},
                      {'name':'Shipping Agency Fees','value':'shippingAgency','j':'cr'}, {'name':'Customs','value':'customs','j':'cr'}, 
                      {'name':'Bromangol','value':'bromangol','j':'cr'}, {'name':'Phytosanitary','value':'localPhytosanitary','j':'cr'}, 
                      {'name':'Cert. Health','value':'certHealth','j':'cr'}, {'name':'Cert. Quality','value':'certQuality','j':'cr'}, 
                      {'name':'Port','value':'port','j':'cr'}, 
                      {'name':'Terminal','value':'terminal','j':'cr'}, {'name':'License','value':'license','j':'cr'}, 
                      {'name':'Forwarding Agency','value':'forwardAgency','j':'cr'},{'name':'Transport','value':'transport','j':'cr'},                  
                      {'name':'Total Fees','value':'totalFees','j':'cr'}, {'name':'Total Cost','value':'totalCost','j':'cr'}, 
                      {'name':'Cost/Pack','value':'costPerPack','j':'cr'}, {'name':'Price/pack @12%','value':'priceAt12','j':'cr'}, 
                      {'name':'Price/pack @12% ($)','value':'priceAt12usd','j':'cr'}, 
                      {'name':'Dest Port','value':'destinationPort','j':'c'},{'name':'Month','value':'month','j':'c'},
                      {'name':'Year','value':'year','j':'c'},{'name':'Updated On','value':'updatedOn','j':'l'}];
      
        this.loadAll(2000,'latest');
        this.populateYear();
        this.getPorts();

        jQuery(this.el.nativeElement).find('#monthSelector li').on('click',function(){  
          jQuery('#monthBtn').html(jQuery(this).text().trim()); 
          jQuery('#monthBtn').attr('value',jQuery(this).text().trim());       
        });

        jQuery(this.el.nativeElement).on('click','#yearSelector li',function(){
          jQuery('#yearBtn').html(jQuery(this).text().trim());
          jQuery('#yearBtn').attr('value',jQuery(this).text().trim()); 
        });


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



  loadAll(year,month){
    if(!this.contains(this.monthNames,month) && month!='latest'){
      return;
    }

    if(Number(year)<2000){
      return;
    }

    var setRev :boolean;
     this.budgetService.getSalesPlan(year,month)
      .subscribe(
          response => {
              this.alertHidden = true;
              this.setData(response);
              this.calSummary();  
          },
          error => {
            if(error.status==404){
               this.setData([]);
               this.popAlert("Info","success","Sales plan for the selected time not found!");          
            }else{
               this.popAlert("Error","danger","Something went wrong, please try again later!");          
            }
          }
        );
  }

  calSummary(){
    this.data.forEach(budget => {
      this.totalFees = this.totalFees+ budget['totalFees'];
      this.totalSalesEst = this.totalSalesEst + budget['totalCost']*1.2
    });
    this.totalItems = this.data.length;
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
        this.selectedMonth = response[0]['month'] ;
        this.selectedYear = response[0]['year'] ;
       this.returnedRange = response[0]['month'] + " " + response[0]['year'];
     }
      this.responseData=response;
      this.data = response;  
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
      this.itemDetail['destinationPort'] = "Select Port";
    }

    popAlert(type,label,msg){
          this.alertHidden = false;
          this.alertType = type;
          this.alertLabel = label;
          this.itemMsg= msg;
    }



estimateDetails(id){

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


    replaceAll(string,old,newStr){
      return string.split('old').join('newStr');
    }

    capitalizeFirstLetter(string) {
      console.log(string);
     return string.charAt(0).toUpperCase() + string.slice(1).toLowerCase();
    }

}
