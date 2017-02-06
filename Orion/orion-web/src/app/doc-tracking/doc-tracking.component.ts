import { Component, OnInit,ElementRef,Renderer,ViewChild } from '@angular/core';
import { MiscService } from '../service/misc.service';
import { FilterNamePipe } from '../pipes/pipe.filterName';
import { DocTrackingService } from './doc-tracking.service';
import { UtilService } from '../service/util.service';
import { OrdersService } from '../orders/orders.service';
import { Router,ActivatedRoute, Params } from '@angular/router';

declare var jQuery : any;

@Component({
  selector: 'app-doc-tracking',
  templateUrl: './doc-tracking.component.html',
  styleUrls: ['./doc-tracking.component.css']
})
export class DocTrackingComponent implements OnInit {
   @ViewChild('reportModalBtn') reportModalBtn:ElementRef;

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
  selectedDate;docType;headerNames=[];trackData;
  activeOrder= {}; filteredSalesPlanList=[];
  allOrdersResponse;allOrders;trackableDocs;richDocList;

  constructor(private utilService :UtilService,private docTrackService:DocTrackingService, private el: ElementRef,
              public route: ActivatedRoute,public router: Router,private orderService:OrdersService,private rd: Renderer) {
    utilService.currentSearchTxt$.subscribe(txt => {this.search(txt);});
    this.trackableDocs = ['Bill of Loading','Proforma Invoice','Commerical Invoice','CNCA','Inspection','Packing List','Certificate of Health','Certificate of Origin','Certificate of Analise',
                      'Certificate of Fumigation','Certificate of Quality','Certificate of Insurance','Certificate of Phytosanitary','Other'];

    this.optionsList = [{'name':'Add New Document Tracking','value':'addNew'}];
    this.utilService.setToolsContent(this.optionsList);
        
    this.pageName;

    //  jQuery(this.el.nativeElement).on('click','.closeAddForm',function(){
    //    jQuery(".inputFormTr").addClass('hidden')});

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

  ngOnInit() {
      this.headers = [{'name':'No','value':'id','j':'x'},{'name':'Order Invoice No','value':'invNo','j':'c'},
                      {'name':'Bill No','value':'bl','j':'c'},{'name':'Documents','value':'documents','j':'l'},
                      {'name':'Courier','value':'courier','j':'c'},{'name':'Tracking Id','value':'trackingId','j':'c'},
                      {'name':'Received On','value':'receivedOn','j':'cd'},{'name':'Received By','value':'receivedBy','j':'c'},
                      {'name':'Remark','value':'remark','j':'c'},{'name':'updated On','value':'updatedOn','j':'c'}];

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



trackItem(id){
   
     
       this.docTrackService.track(id)
      .subscribe(
          response => {
              this.trackData =null;
              var res = JSON.parse(response['_body']);
            if(res['type']=="HTML"){
              jQuery("#htmlTracking").html(res['data']);
            }else{
              var resultArray=[];
              for (var key in res['data'][0]) {
                  this.headerNames.push(key);
               }
               this.trackData = res['data'];
            }
            let event = new MouseEvent('click', {bubbles: true});
            this.rd.invokeElementMethod(
            this.reportModalBtn.nativeElement, 'dispatchEvent', [event]); 
          },
          error => {
               console.error("tracking error!");          
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
    var modalInfo = {"title" : "Order", "msg" : this.activeProductHeader.split('-')[1],"task" :"myTask"};
    this.utilService.showModalState(modalInfo);
}

  loadAll(orderId){
    if(orderId==null || orderId=='')return;
     this.docTrackService.get(orderId)
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



  updateSelected(type,notSelected){
    var newApproval = [];
    var selected;
    this.richDocList.forEach(el =>{
      if(el.selected || (type.trim() == el.type.trim() && !notSelected)){
        newApproval.push(el.type);
      }
    });
    this.itemDetail['documents'] =  newApproval.join(",");
  }


    getRichList(selectedList){
      if(selectedList!=null){
         var selList = selectedList.split(',');
      }
      var allList = JSON.parse(JSON.stringify(this.trackableDocs));
      this.richDocList =[];
      allList.forEach(el => {
             
            if(selectedList!=null && selList.indexOf(el)>-1){
              this.richDocList.push({'type' :el, 'selected' : true});
            }else{
              this.richDocList.push({'type' :el, 'selected' : false});
            }
      });
    
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
      this.docTrackService.deleteById(this.activeDocId,id)
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
            this.docTrackService.add(this.itemDetail,this.activeDocId)
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
            this.docTrackService.update(this.itemDetail,this.activeDocId)
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

