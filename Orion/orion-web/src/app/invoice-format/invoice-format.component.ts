import { Component, OnInit,ElementRef,Renderer,ViewChild} from '@angular/core';
import { MiscService } from '../service/misc.service';
import { FilterNamePipe } from '../pipes/pipe.filterName';
import { ContainerService } from '../container/container.service';
import { UtilService } from '../service/util.service';
import { InvoiceFormatService } from './invoice-format.service';
import { Router,ActivatedRoute, Params } from '@angular/router';

declare var $ : any;

@Component({
  selector: 'app-invoice-format',
  templateUrl: './invoice-format.component.html',
  styleUrls: ['./invoice-format.component.css']
})
export class InvoiceFormatComponent implements OnInit {
  @ViewChild('reportModalBtn') modalInput:ElementRef;
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

  constructor(private utilService :UtilService,private el: ElementRef,private rd: Renderer,
              private invFrmtService:InvoiceFormatService ,public route: ActivatedRoute,public router:Router) {
          utilService.currentSearchTxt$.subscribe(txt => {this.search(txt);});
          utilService.currentdelItem$.subscribe(opt => { this.delete();}); 
          this.optionsList = [{'name':'Add Invoice Format','value':'addNew'}];
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
      this.headers = [{'name':'No','value':'id','j':'x'},{'name':'Exporter','value':'exporter','j':'l'},
                      {'name':'Invoice Type','value':'invoiceType','j':'l'},{'name':'Format File','value':'format','j':'l'},
                      {'name':'Updated No','value':'updatedOn','j':'c'}];
        
        this.activeShippingId = this.route.snapshot.params['id'];
        this.loadAll();

  } 



triggerDelModal(event){
    event.preventDefault();
    var modalInfo = {"title" : "Order", "msg" : this.activeProductHeader.split('-')[1],"task" :"myTask"};
    this.utilService.showModalState(modalInfo);
}


  loadAll(){

    var setRev :boolean;
     this.invFrmtService.get()
      .subscribe(
          response => {
              this.setData(response);    
          },
          error => {
            if(error.status==404){
               this.popAlert("Info","Info","Invoice format is not yet added!");          
            }else{
               this.popAlert("Error","danger","Something went wrong, please try again later!");          
            }
          }
        );
  }

 preview(item){
    this.invFrmtService.getInvoiceFormat(item['id'])
      .subscribe(
          response => {
               $("#reportModalDiv").html(response['_body']); 
                let event = new MouseEvent('click', {bubbles: true});
                this.rd.invokeElementMethod(
                this.modalInput.nativeElement, 'dispatchEvent', [event]); 
          },
          error => {
            if(error.status==404){
               this.popAlert("Info","Info","Preview not found!");          
            }else{
              console.log();
               this.popAlert("Error","danger","Something went wrong, please try again later!");          
            }
          }
        );
}


close(){
  console.log("closing");
  $("#reportModalDiv").empty();  
}

  setData(response){
      this.responseData=response;
      this.data = response;  
  }


    addUpdate(event){
      event.preventDefault();
        if (this.taskType=="Add"){
            this.invFrmtService.add(this.itemDetail)
            .subscribe(
                response => {
                    this.popAlert("Info","success","Invoice format successfully added!");
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
      this.invFrmtService.deleteById(id)
      .subscribe(
          response => {
              this.setData(response);  
              this.popAlert("Info","success","Invoice format successfully deteled!"); 
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
            this.invFrmtService.update(this.itemDetail)
            .subscribe(
                response => {
                    this.hideAddNewForm = true;
                    this.popAlert("Info","success","Invoice format successfully updated!");
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


