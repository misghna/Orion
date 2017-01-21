import { Component, OnInit,ElementRef} from '@angular/core';
import { MiscService } from '../service/misc.service';
import { FilterNamePipe } from '../pipes/pipe.filterName';
import { ContainerService } from '../container/container.service';
import { UtilService } from '../service/util.service';
import { TerminalService } from './terminal.service';
import { Router,ActivatedRoute, Params } from '@angular/router';

declare var jQuery : any;

@Component({
  selector: 'app-terminal',
  templateUrl: './terminal.component.html',
  styleUrls: ['./terminal.component.css']
})
export class TerminalComponent implements OnInit {
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
              private terminalService:TerminalService ,public route: ActivatedRoute,public router:Router) {
          
          utilService.currentdelItem$.subscribe(opt => { this.delete();}); 
          this.optionsList = [{'name':'Add Terminal','value':'addNew'}];
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
      this.headers = [{'name':'No','value':'id','j':'x'},{'name':'Terminal Name','value':'name','j':'l'},
                      {'name':"Offload Fee 20'/cont",'value':'offloadFee20ft','j':'l'},{'name':"Offload Fee 40'/cont",'value':'offloadFee40ft','j':'c'},
                      {'name':'Admin & Service Charge/bill','value':'adminServiceCharge','j':'c'},{'name':'Free days','value':'freeDays','j':'l'},
                      {'name':'1st Storage days','value':'storageFirstRangeDays','j':'l'},{'name':"1st Range Rate/cont20'",'value':'storageFirstRangeFee20ft','j':'c'},
                      {'name':"1st Range Rate/cont40'",'value':'storageFirstRangeFee40ft','j':'c'},
                      {'name':'2nd Storage days','value':'storageSecondRangeDays','j':'l'},{'name':'2nd Storage Rate/cont20','value':'storageSecondRangeFee20ft','j':'l'},
                      {'name':'2nd Range Rate/cont40','value':'storageSecondRangeFee40ft','j':'l'},
                      {'name':'Transport fees/cont','value':'transport','j':'l'},{'name':'Tarrif/cont','value':'importTarrif','j':'l'},
                      {'name':'Others %','value':'otherPercent','j':'l'}];
        
        this.activeShippingId = this.route.snapshot.params['id'];
        this.loadAll();
        
   //     this.getAllShiping();

        jQuery(this.el.nativeElement).find('#monthSelector li').on('click',function(){  
          jQuery('#monthBtn').html(jQuery(this).text().trim()); 
          jQuery('#monthBtn').attr('value',jQuery(this).text().trim());       
        });

  } 



triggerDelModal(event){
    event.preventDefault();
    console.log("triigered");
    var modalInfo = {"title" : "Order", "msg" : this.activeProductHeader.split('-')[1],"task" :"myTask"};
    this.utilService.showModalState(modalInfo);
}


  loadAll(){

    var setRev :boolean;
     this.terminalService.get()
      .subscribe(
          response => {
              this.setData(response);    
          },
          error => {
            if(error.status==404){
               this.popAlert("Info","Info","Terminal is not yet added!");          
            }else{
               this.popAlert("Error","danger","Something went wrong, please try again later!");          
            }
          }
        );
  }


  setData(response){
      this.responseData=response;
      this.data = response;  
  }


    addUpdate(event){
      event.preventDefault();
        if (this.taskType=="Add"){
            this.terminalService.add(this.itemDetail)
            .subscribe(
                response => {
                    this.popAlert("Info","success","Terminal successfully added!");
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
      this.terminalService.deleteById(id)
      .subscribe(
          response => {
              this.setData(response);  
              this.popAlert("Info","success","Terminal Agency successfully deteled!"); 
          },
          error => {
            if(error.status==404){
              this.setData([]);
               this.popAlert("Info","Info","Terminal is not yet added!");          
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

    update(){
            this.terminalService.update(this.itemDetail)
            .subscribe(
                response => {
                    this.hideAddNewForm = true;
                    this.popAlert("Info","success","Terminal successfully updated!");
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


