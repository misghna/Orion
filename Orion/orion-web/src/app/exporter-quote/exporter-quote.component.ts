import { Component, OnInit,ElementRef,ViewChild,Renderer} from '@angular/core';
import { MiscService } from '../service/misc.service';
import { FilterNamePipe } from '../pipes/pipe.filterName';
import { ContainerService } from '../container/container.service';
import { UtilService } from '../service/util.service';
import { ExporterQuoteService } from './exporter-quote.service';
import { Router,ActivatedRoute, Params } from '@angular/router';
import { BidService } from '../bid/bid.service';
import { UserService } from '../users/users.service';
import { ApprovalService } from '../approval/approval.service';
import { CurrencyService } from '../currency/currency.service';
import { AddressBookService } from '../address-book/address-book.service';

declare var jQuery : any; 

@Component({
  selector: 'app-exporter-quote',
  templateUrl: './exporter-quote.component.html',
  styleUrls: ['./exporter-quote.component.css']
})
export class ExporterQuoteComponent implements OnInit {
  @ViewChild('mdlCloseBtn') modalInput:ElementRef;
  data;responseData;itemMsg;
  alertType;alertHidden;alertLabel;
  hideAddNewForm;  hideLoader;
  itemDeleteId;
  itemDetail={};
  approversList;approversPlaceHolder;approvalAlert={};
  taskType = "Add";
  activeItemId;
  activeProductHeader;
  pageName;
  optionsList;constantOptionList;
  todayDate;
  monthNames;
  productNameList=[]; allPrdList=[];
  headers = [];currencies;responseCurrency;
  filterQuery = "";
  bntOption = "Search";addresses=[];
  selectedDate;activeShippingId;activeOrderId;
  activeOrder= {}; filteredSalesPlanList=[]; allOrders;shippingList; allShippingList;

  constructor(private utilService :UtilService,private el: ElementRef,private rd: Renderer,
              private exporterQuoteService:ExporterQuoteService ,private userService :UserService,
              public route: ActivatedRoute,public router:Router, private bidService :BidService,
              private approvalService :ApprovalService,private currencyService :CurrencyService,
              private addressBookService :AddressBookService) {

          this.approvalAlert['hidden']=true;
          utilService.currentSearchTxt$.subscribe(txt => {this.search(txt);});
          utilService.currentdelItem$.subscribe(opt => { this.delete();}); 
          this.optionsList = [];
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
      this.headers = [{'name':'No','value':'id','j':'x'},{'name':'Inv No','value':'invNo','j':'l'},
                      {'name':'Desc','value':'descr','j':'l'},{'name':'Total Packs','value':'totalPacks','j':'l'},
                      {'name':'Source','value':'source','j':'c'}, {'name':'Exporter','value':'exporter','j':'c'},
                      {'name':'Origin City','value':'origin','j':'c'},{'name':'Currency','value':'currency','j':'c'},
                      {'name':'Fob','value':'fob','j':'c'},
                      {'name':'Fob Margin(%)','value':'fobMargin','j':'c'},{'name':'Adj. Fob','value':'adjFob','j':'c'},
                      {'name':'Freight','value':'freight','j':'c'},{'name':'Freight Margin(%)','value':'freightMargin','j':'c'},
                      {'name':'Adj. Freight','value':'adjFreight','j':'c'},{'name':'Unit price','value':'unitPrice','j':'cr'},
                      {'name':'Total Price','value':'totalPrice','j':'cr'},{'name':'Status','value':'status','j':'c'},
                      {'name':'updated On','value':'updatedOn','j':'c'}];
        
        this.activeShippingId = this.route.snapshot.params['id'];
        this.loadAll();
        this.getAllCurrencies();

        jQuery(this.el.nativeElement).find('#monthSelector li').on('click',function(){  
          jQuery('#monthBtn').html(jQuery(this).text().trim()); 
          jQuery('#monthBtn').attr('value',jQuery(this).text().trim());       
        });

        this.getAddressByType("Exporter");

  } 



triggerDelModal(event){
    event.preventDefault();
    console.log("triigered");
    var modalInfo = {"title" : "Order", "msg" : this.activeProductHeader.split('-')[1],"task" :"myTask"};
    this.utilService.showModalState(modalInfo);
}


  loadAll(){

    var setRev :boolean;
     this.exporterQuoteService.getAll()
      .subscribe(
          response => {
              this.setData(response);    
          },
          error => {
            if(error.status==404){
               this.popAlert("Info","Info","Quote is not yet added!");          
            }else{
               this.popAlert("Error","danger","Something went wrong, please try again later!");          
            }
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

filterCurrency(txt){
  this.currencies = this.currencyService.filterCurrency(txt,this.responseCurrency);
}

updateFields(field){
  switch(field){
    case "fob" :
    case "fobMargin" :  
      if(this.itemDetail['fobMargin']==null)this.itemDetail['fobMargin']=0;
      var margin = this.round((this.itemDetail['fob'] * this.itemDetail['fobMargin'])/100);
      this.itemDetail['adjFob'] = parseFloat(this.itemDetail['fob']) + margin;
      break;
    case "fobAdj" :
      var fob = parseFloat(this.itemDetail['fob']);
      this.itemDetail['fobMargin'] = this.round((this.itemDetail['adjFob'] - fob)/fob *100);
      break;
    case "freight" :
    case "freightMargin" :
      if(this.itemDetail['freightMargin']==null)this.itemDetail['freightMargin']=0;
      var margin = this.round((this.itemDetail['freight'] * this.itemDetail['freightMargin'])/100);
      this.itemDetail['adjFreight'] = parseFloat(this.itemDetail['freight']) + margin;
      break;
    case "freightAdj" :
      var fob = parseFloat(this.itemDetail['freight']);
      this.itemDetail['freightMargin'] = this.round((this.itemDetail['adjFreight'] - fob)/fob *100);
      break;      
      default :
  }
 // console.log(this.itemDetail['adjFob'] + this.itemDetail['adjFreight']);
 // console.log( Math.round(parseFloat(this.itemDetail['adjFob'])+ parseFloat(this.itemDetail['adjFreight'])));
      this.itemDetail['unitPrice'] = Math.round((parseFloat(this.itemDetail['adjFob'])+ parseFloat(this.itemDetail['adjFreight']))*100)/100;      
      this.itemDetail['totalPrice'] = Math.round((parseFloat(this.itemDetail['unitPrice'])+ parseFloat(this.itemDetail['totalPacks']))*100)/100; 
}

getBid(orderRef){
     this.bidService.getBidWinner(orderRef)
      .subscribe(
          response => {
              this.itemDetail['origin'] = response['itemOrigin'];
              this.itemDetail['currency'] = response['currency'];
              this.itemDetail['fob'] = response['fob'];
              this.itemDetail['fobMargin'] = 0.0;
              this.itemDetail['adjFob'] = response['fob'];

              this.itemDetail['freight'] = this.round(response['cifCnf']-response['fob']);
              this.itemDetail['freightMargin'] = 0.0;
              this.itemDetail['adjFreight'] =  this.round(response['cifCnf']-response['fob']);

              this.itemDetail['totalPacks'] = this.itemDetail['pckPerCont'] * this.itemDetail['contQnt'];
              this.itemDetail['unitPrice'] = response['fob'] + this.itemDetail['adjFreight'];
              this.itemDetail['totalPrice'] = this.itemDetail['unitPrice'] * this.itemDetail['totalPacks'];
              
          },
          error => {
            if(error.status==404){
               this.popAlert("Info","Info","Bid winner not found please add bidding details before you continue, or select different source");          
            }else{
               this.popAlert("Error","danger","Something went wrong, please try again later!");          
            }
          }
        );
}

 round(value) {
  return Math.round(value*100)/100;
}

  setData(response){
  //  var obj = {};
     response.forEach(el => {
       el['descr'] = el.item +' (' + el.brand + ')';
       el['totalPacks'] = el.pckPerCont * el.contQnt;
     });
      this.responseData=response;
      this.data = response;  
  }


    addUpdate(event){
      event.preventDefault();
        if(this.itemDetail['source']==null){
          this.popAlert("Error","danger","Please select Item source!");
          return;
        }
        if(this.itemDetail['exporter']==null){
          this.popAlert("Error","danger","Please select Exporter!");
          return;
        }
        if(!this.checkCurrency(this.itemDetail['currency'])){
          this.popAlert("Error","danger","Please select Currency from list!");
          return;
        }
        if (this.taskType=="Add"){
            this.exporterQuoteService.add(this.itemDetail)
            .subscribe(
                response => {
                    this.popAlert("Info","success","Setting successfully added!");
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

    checkCurrency(cur){
      var found = false;
      this.responseCurrency.forEach(el => {
        if(el.abrevation==cur){
          found = true;
        }
      });
      return found;
    }

   getMarginApprovers(item){
     this.itemDetail = item;
     if(this.itemDetail['totalPrice']==null){
        this.popAlert("Error","danger","Please enter price details!");
        let event = new MouseEvent('click', {bubbles: true});
        this.rd.invokeElementMethod(this.modalInput.nativeElement, 'dispatchEvent', [event]);  
       return;
     }
     this.userService.getApprovers("Exporter Margin")
      .subscribe(
          response => {
              this.approversList = response; 
          },
          error => {
            console.log("approvers not found");
               this.approversPlaceHolder = "Approvers not found" ;      
          }
        );
}

 reqApproval(event){
    event.preventDefault();

    // console.log("value '" + orderRef + "'");
      if(this.itemDetail['approver']=='' || this.itemDetail['approver']==null){
        this.approvalAlert= {'hidden':false,'content':'Please select approver from list'};
        return;
      }
      var body = {'forId':this.itemDetail['id'], 'type':'Exporter Margin','forName':'Exporter Margin',
                  'orderRef':this.itemDetail['id'],'approver':this.itemDetail['approver']};
      console.log(body);
      this.approvalService.add(body)
      .subscribe(
          response => {
              //close modal 
             let event = new MouseEvent('click', {bubbles: true});
             this.rd.invokeElementMethod(this.modalInput.nativeElement, 'dispatchEvent', [event]);
           
             // inform
              this.loadAll();
              this.approvalAlert['hidden']= true;
              this.popAlert("Info","success","Approval Request submited!");    
          },
          error => { 
            if(error.status = 'undefined'){
              let event = new MouseEvent('click', {bubbles: true});
              this.rd.invokeElementMethod(this.modalInput.nativeElement, 'dispatchEvent', [event]);
              this.loadAll();
              this.approvalAlert['hidden']= true;
              this.popAlert("Info","success","Approval Request submited!"); 
            }else{
              this.popAlert("Error","danger",this.utilService.getErrorMsg(error));
            }
          }
        );
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
      this.exporterQuoteService.deleteById(id)
      .subscribe(
          response => {
              this.setData(response);  
              this.popAlert("Info","success","Quote successfully deteled!"); 
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
            this.exporterQuoteService.update(this.itemDetail)
            .subscribe(
                response => {
                    this.hideAddNewForm = true;
                    this.popAlert("Info","success","Setting successfully updated!");
                    this.setData(response);     
                },
                error => {
                    if(error.status==400){
                      this.popAlert("Error","danger",this.utilService.getErrorMsg(error));
                    } else{
                      this.popAlert("Error","danger","Something went wrong, please try again later!");
                    }     
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


  getAddressByType(type){
     this.addressBookService.get(type)
      .subscribe(
          response => {
              this.addresses = response;    
          },
          error => {
            console.log("exporters not found");
          }
        );
  }
}



