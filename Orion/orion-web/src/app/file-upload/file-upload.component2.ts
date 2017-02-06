import { Component, OnInit,ElementRef } from '@angular/core';
import { FileUploader } from 'ng2-file-upload';
import { UtilService } from '../service/util.service';
import Utils  from '../service/utility';
import { Router,ActivatedRoute, Params } from '@angular/router';
import { OrdersService } from '../orders/orders.service';
import { DocumentService } from '../document/document.service';

declare var $ :any;

//const URL = 'https://evening-anchorage-3159.herokuapp.com/api/';


@Component({
  selector: 'app-file-upload',
  templateUrl: './file-upload.component.html',
  styleUrls: ['./file-upload.component.css']
})
export class FileUploadComponent implements OnInit {

  public uploader:FileUploader = new FileUploader({url: 'uploadUrl'});
  public hasBaseDropZoneOver:boolean = false;
  public hasAnotherDropZoneOver:boolean = false;
  callCount=0;compId=0;
  responseData; data;
  optionsList;fData=[]; alertWin={};  lastFileOpenerTime=0; headers = [];activeDocId='';
  docTypes;uploadType;URL;allOrdersResponse;allOrders;itemDetail={};
  selectedDate;docType;activeOrder= {}; filteredSalesPlanList=[];
  constructor(private el : ElementRef,private utilService: UtilService,public route: ActivatedRoute,public router: Router,
  private orderService :OrdersService,private docService :DocumentService){
  //  this.URL = Utils.getBaseUrl() + 'api/user/uploadFile/';
   this.compId= Math.random()
    console.log("created");
    this.alertWin = {'alertHidden':true,'type':'','label':'','msg':''}
    this.optionsList = [{'name':'Upload Document','value':'addNew'}];
    this.utilService.setToolsContent(this.optionsList);
    utilService.currentToolsOptCont$.subscribe(
      opt => {  
        this.callCount = this.callCount +1;
        console.log(this.compId + "-" +  this.callCount);
        this.option(opt);
    });
    this.docTypes = ['Bill of Loading','Commerical Invoice','CNCA','Certificate of Health','Certificate of Origin','Certificate of Analise',
                      'Certificate of Fumigation','Certificate of Quality',
                      'Certificate of Insurance','Du License','Inspection','Local Phytosanitary','Packing List','Proforma Invoice','Other'];

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


  public fileOverBase(e:any):void {
    this.hasBaseDropZoneOver = e;
  }

  public fileOverAnother(e:any):void {
    this.hasAnotherDropZoneOver = e;
  }

  ngOnInit() {
    // console.log(this.uploader.);
    this.uploader.options.url = Utils.getBaseUrl() + 'api/user/uploadFile/';
    this.uploader.onBuildItemForm = (fileItem: any, form: any) => {
      var attachDoc = this.getFileDate(fileItem.file.name)
      // console.log(JSON.stringify(attachDoc));
      form.append('data', JSON.stringify(attachDoc[0]));
      form.append('orderRef', this.itemDetail['orderRef']);
    };

    this.getAllOrders();

    this.uploader.onAfterAddingFile =(fileItem: any) => {
       this.fData.push({'file':fileItem.file.name,'name' : '' , 'type' :'Select Type','remark':''});
    }

    this.uploader.onSuccessItem=(fileItem: any, response: string, status: number, headers: any)=>{
      this.utilService.setLoaderState(false);
      this.popAlert("Info","success","Document(s) uploaded successfully!"); 
      this.utilService.setDocList(JSON.parse(response));
        setTimeout(() => {
          if(this.uploadType=='multi'){
             this.uploader.clearQueue();
          }  
      }, 2000)  
    }

    // load documents 

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

changeType(i,type){
  this.fData[i].type = type;
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

  setData(response){
      this.responseData=response;
      this.data = response;  
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

  uploadAllFiles(event){
      event.preventDefault();
      this.uploadType='multi';
      var dataInvalid = false;
      console.log(JSON.stringify(this.itemDetail));
      if((JSON.stringify(this.itemDetail)) == '{}'){
        this.popAlert("Error","Danger","select InvNo or BL & try again!"); 
         return;
      }
      this.fData.forEach((data, index) => {
          if(data.type=='Select Type'){
            this.popAlert("Error","Danger","select Type for all the document!"); 
            dataInvalid = true;
          }
      });
      if(!dataInvalid){
        this.utilService.setLoaderState(true);
        this.uploader.uploadAll()
      }
  }

uploadOneFile(item){
    this.uploadType='single';
    var dataInvalid = false;

    if((JSON.stringify(this.itemDetail)) == '{}'){
      this.popAlert("Error","Danger","select InvNo or BL & try again!"); 
        return;
    }

    this.fData.forEach((data, index) => {
        if(data.file == item.file.name && data.type=='Select Type'){
          this.popAlert("Error","Danger","select Type for the documents!"); 
          dataInvalid = true;
        }
    });
    if(!dataInvalid){
      this.utilService.setLoaderState(true);
      item.upload();
    }
}



  popAlert(type,label,msg){
        this.alertWin['alertHidden'] = false;
        this.alertWin['type'] = type;
        this.alertWin['label'] = label;
        this.alertWin['msg']= msg;
  }



getFileDate(fileName){
  return this.fData.filter(data => {
          if(data!=null && data.file != null && data.file==fileName ) return data;
        });
}

  openUploaderWindow(){
     $(this.el.nativeElement).find("#fileSelector").click();
  //   this.lastFileOpenerTime=0;
  }

    option(options){
      var selected = options.optionName;
      var countDash = (selected.match(/-/g) || []).length;
      switch(true){
        case (selected == 'addNew') :        
             this.openUploaderWindow();           
          break;
        default:
          console.log(selected);
      }
    }

}
