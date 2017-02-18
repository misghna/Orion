import { Component, OnInit,ElementRef} from '@angular/core';
import { MiscService } from '../service/misc.service';
import { FilterNamePipe } from '../pipes/pipe.filterName';
import { ContainerService } from '../container/container.service';
import { UtilService } from '../service/util.service';
import { DocHandoverService } from './doc-handover.service';
import { Router,ActivatedRoute, Params } from '@angular/router';
import { ClientService } from '../client/client.service';
import { OrdersService } from '../orders/orders.service';



declare var jQuery : any;

@Component({
  selector: 'app-doc-handover',
  templateUrl: './doc-handover.component.html',
  styleUrls: ['./doc-handover.component.css']
})
export class DocHandoverComponent implements OnInit {
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
  bntOption = "Search";fwAgents;
  selectedDate;activeShippingId;activeOrderId;
  activeOrder= {}; filteredSalesPlanList=[]; allOrders;shippingList; allShippingList;
  allOrdersResponse;richDocList;trackableDocs;

  constructor(private utilService :UtilService,private el: ElementRef,
              private docHandService:DocHandoverService ,public route: ActivatedRoute,public router:Router, 
              private clientService :ClientService, private orderService :OrdersService) {

        this.trackableDocs = ['Bill of Loading','Proforma Invoice','Commerical Invoice','CNCA','Inspection','Packing List','Certificate of Health','Certificate of Origin','Certificate of Analise',
                      'Certificate of Fumigation','Certificate of Quality','Certificate of Insurance','Certificate of Phytosanitary','Other'];

          utilService.currentSearchTxt$.subscribe(txt => {this.search(txt);});
          utilService.currentdelItem$.subscribe(opt => { this.delete();}); 
          this.optionsList = [{'name':'Add Document Handover','value':'addNew'}];
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
      this.headers = [{'name':'No','value':'id','j':'x'},{'name':'Inv No.','value':'invNo','j':'l'},
                      {'name':'BL','value':'bl','j':'l'},{'name':'Submitted by','value':'receivedFrom','j':'l'},
                      {'name':'Recipient','value':'receivedBy','j':'l'},{'name':'Document List','value':'docs','j':'c'},
                      {'name':'Received On','value':'receivedOn','j':'cd'},{'name':'Returned To','value':'returnedTo','j':'c'},
                      {'name':'Returned On','value':'returnedOn','j':'cd'},{'name':'Status','value':'status','j':'l'}];
        
        this.activeShippingId = this.route.snapshot.params['id'];
        this.loadAll();
        this.getFWAgents();
        this.getAllOrders();

  } 



triggerDelModal(event){
    event.preventDefault();
    console.log("triigered");
    var modalInfo = {"title" : "Order", "msg" : "record of documents submited to  " + this.activeProductHeader.split('-')[1],"task" :"myTask"};
    this.utilService.showModalState(modalInfo);
}


  loadAll(){

    var setRev :boolean;
     this.docHandService.get()
      .subscribe(
          response => {
              this.setData(response);    
          },
          error => {
             this.sendError(error);
          }
        );
  }


  setData(response){
      this.responseData=response;
      this.data = response;  
  }


getFWAgents(){
     this.clientService.getFWAgents()
      .subscribe(
          response => {
              this.fwAgents = response;    
          },
          error => {
            error.log("Clients/WH not found");
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


  updateSelected(type,notSelected){
    var newApproval = [];
    var selected;
    this.richDocList.forEach(el =>{
      if(el.selected || (type.trim() == el.type.trim() && !notSelected)){
        newApproval.push(el.type);
      }
    });
    this.itemDetail['docs'] =  newApproval.join(",");
  }

    addUpdate(event){
      event.preventDefault();
        if(this.itemDetail['receivedBy']=='Select Recipient'){
          this.popAlert("Error","danger","Please select Recipient from the list");
          return;
        }
        if(this.itemDetail['orderRef']==null){
          this.popAlert("Error","danger","Please select InvNo or Bl from the list");
          return;
        }
        if (this.taskType=="Add"){
            this.docHandService.add(this.itemDetail)
            .subscribe(
                response => {
                    this.popAlert("Info","success","Handover form created, Email has been sent to the recipient for confirmation!");
                    this.setData(response);  
                    this.hideAddNewForm = true;  
                },
                error => {
                     this.sendError(error);
                  }
              );
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
      this.itemDetail['receivedBy']='Select Recipient';
    }

    popAlert(type,label,msg){
          this.alertHidden = false;
          this.alertType = type;
          this.alertLabel = label;
          this.itemMsg= msg;
    }

    delete(){
      var id = this.activeProductHeader.split('-')[0];
      this.docHandService.deleteById(id)
      .subscribe(
          response => {
              this.setData(response);  
              this.popAlert("Info","success","Setting successfully deteled!"); 
          },
          error => {
              this.sendError(error);
          }
        );
    }


   sendError(error){
        if(error.status==404){
          this.popAlert("Info","Info","No record of document handover!");  
          this.setData([]);        
        }else if(error.status==400){
          this.popAlert("Error","danger",this.utilService.getErrorMsg(error));          
        }else{
          this.popAlert("Error","danger","Something went wrong, please try again later!");   
        }
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

    markAsReturned(idd){
      var id = idd.split('-')[0];
        this.docHandService.markAsReturned(id)
            .subscribe(
                response => {
                    this.hideAddNewForm = true;
                    this.popAlert("Info","success","Document return confirmation email has been sent to the recipient!");
                    this.setData(response);     
                },
                error => {      
                     this.sendError(error);
                }
              );
    }


    // update(){
    //       console.log(this.itemDetail['id']);
    //         this.docHandService.update(this.itemDetail)
    //         .subscribe(
    //             response => {
    //                 this.hideAddNewForm = true;
    //                 this.popAlert("Info","success","Setting successfully updated!");
    //                 this.setData(response);     
    //             },
    //             error => {      
    //                  this.sendError(error);
    //             }
    //           );
    // }

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


