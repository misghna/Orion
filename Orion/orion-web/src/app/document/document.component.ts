import { Component, OnInit,ElementRef} from '@angular/core';
import { MiscService } from '../service/misc.service';
import { FilterNamePipe } from '../pipes/pipe.filterName';
import { DocumentService } from '../document/document.service';
import { UtilService } from '../service/util.service';
import { OrdersService } from '../orders/orders.service';
import { Router,ActivatedRoute, Params } from '@angular/router';

declare var jQuery : any;

@Component({
  selector: 'app-document',
  templateUrl: './document.component.html',
  styleUrls: ['./document.component.css']
})
export class DocumentComponent implements OnInit {
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
  headers = [];activeDocId='';
  filterQuery = "";
  bntOption = "Search";
  selectedDate;docType;
  activeOrder= {}; filteredSalesPlanList=[];
  compId=0;
  constructor(private utilService :UtilService,private docService:DocumentService, private el: ElementRef,
              public route: ActivatedRoute,public router: Router,private orderService:OrdersService) {
    this.compId= Math.random();
    utilService.setDocInstId(this.compId);
    utilService.currentSearchTxt$.subscribe(txt => {this.search(txt);});
    this.optionsList = [{'name':'Upload New Document','value':'addNew'}];
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

    utilService.currentdelItem$.subscribe(
      opt => {  
        this.delete(opt);
    });

    utilService.currentDocList$.subscribe(
      docs => {  
       this.setData(docs);  
    });

    router.events.subscribe((val) => {
      var newRouteParam = this.route.snapshot.params['id'];
      if(this.activeDocId != newRouteParam){
        this.activeDocId = newRouteParam;
          this.loadAll(this.activeDocId);
          if(this.activeDocId.indexOf("orderRef")>=0){
            this.getActiveOrder()
          }else{
            this.activeOrder = null;
            this.docType = this.activeDocId.replace(/\_/g,' ')
          }
      }       
    });

   }

  ngOnInit() {
      this.headers = [{'name':'No','value':'id','j':'x'},
                      {'name':'Order Inv. No','value':'invNo','j':'c'},{'name':'Bill No','value':'bl','j':'c'},
                      {'name':'Type','value':'type','j':'l'},{'name':'Remark','value':'remark','j':'c'},
                      {'name':'Updated On','value':'updatedOn','j':'c'}];
          var activeOrderId = this.route.snapshot.params['id'];
          if(this.activeDocId.indexOf("orderRef")>=0){
            this.getActiveOrder()
          }else{
            this.activeOrder = null;
            this.docType = this.activeDocId.replace(/\_/g,' ')
          }
        this.loadAll(this.activeDocId);

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

triggerDelModal(event){
    event.preventDefault();
    console.log("triigered");
    var modalInfo = {"title" : "Order", "msg" : this.activeProductHeader.split('-')[1],"task" :"myTask"};
    this.utilService.showModalState(modalInfo);
}



  execute(task){
    if(task =="Search"){
    //  this.loadAll(this.selectedYear,this.selectedMonth);
    }
  }


  loadAll(orderId){
    if(orderId==null || orderId=='')return;
     this.docService.get(orderId)
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
      this.docService.deleteById(this.activeDocId,id)
      .subscribe(
          response => {
              this.setData(response);  
              this.popAlert("Info","success","Document successfully deteled!"); 
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

    editItem(idd){        
      var id = idd.split('-')[0];
      this.activeItemId = id;
      this.taskType = "Update";
      this.itemDetail = this.responseData.filter(item => item.id==id)[0];
      this.getDate(new Date());
      this.hideAddNewForm=false;
    }

    update(){
          console.log(this.itemDetail['id']);
            this.docService.update(this.itemDetail)
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


    // option(options){
    //   var selected = options.optionName;
    //   var countDash = (selected.match(/-/g) || []).length;
    //   switch(true){
    //     case (selected == 'addNew') :
    //        this.showAddForm();
    //       break;
    //     default:
    //       console.log(selected);
    //   }
    // }

   openUploaderWindow(){
     jQuery(this.el.nativeElement).find("#fileSelector").click();
  //   this.lastFileOpenerTime=0;
  }

  openFile(path){
    window.open(
            this.utilService.getBaseUrl()+ path,'_blank'
    );

  }
    option(options){
      var date = new Date();
      var selected = options.optionName;
      var countDash = (selected.match(/-/g) || []).length;
      switch(true){
        case (selected == 'addNew') :        
           if(this.router.url.indexOf('document/uploaded')>0 && this.compId==this.utilService.getDocInstId()){
             this.openUploaderWindow();           
           }
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

