import { Component, OnInit,ElementRef } from '@angular/core';
import { FileUploader } from 'ng2-file-upload';
import { UtilService } from '../service/util.service';
import Utils  from '../service/utility';
import { Router,ActivatedRoute, Params } from '@angular/router';
import { OrdersService } from '../orders/orders.service';

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

  optionsList;fData=[]; alertWin={};  lastFileOpenerTime=0;
  docTypes;uploadType;activeDocId;URL;allOrdersResponse;allOrders;itemDetail={};
  constructor(private el : ElementRef,private utilService: UtilService,public route: ActivatedRoute,public router: Router,
  private orderService :OrdersService){
  //  this.URL = Utils.getBaseUrl() + 'api/user/uploadFile/';

    this.alertWin = {'alertHidden':true,'type':'','label':'','msg':''}
    // this.optionsList = [{'name':'Upload Document','value':'addNew'}];
    // this.utilService.setToolsContent(this.optionsList);

    this.docTypes = ['Bill of Loading','Commerical Invoice','CNCA','Certificate of Health','Certificate of Origin','Certificate of Analise',
                      'Certificate of Fumigation','Certificate of Quality',
                      'Certificate of Insurance','Du License','Inspection','Local Phytosanitary','Packing List','Proforma Invoice','Other'];

    
    // utilService.currentToolsOptCont$.subscribe(
    //   opt => {  
    //     this.option(opt);
    // });


    router.events.subscribe((val) => {
      var newRouteParam = this.route.snapshot.params['id'];
      if(this.activeDocId != newRouteParam){
          this.activeDocId = newRouteParam;
          if(newRouteParam.indexOf("orderRef")>=0){
//            this.utilService.setToolsContent(this.optionsList);
          }else{
//            this.utilService.setToolsContent(null);
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
          window.location.reload(true);  
      }, 2000)  
    }

    this.uploader.onErrorItem = (item:any, response:any, status:any, headers:any) => {
      console.log(status);
      this.popAlert("Error","Danger","File upload failed [max file size allowed is 10Mb]"); 
      this.utilService.setLoaderState(false);
    };


  }

changeType(i,type){
  this.fData[i].type = type;
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
      var date = new Date();
      var selected = options.optionName;
      var countDash = (selected.match(/-/g) || []).length;
      switch(true){
        case (selected == 'addNew') :        
           var diff = date.getTime() - this.lastFileOpenerTime;
           console.log(diff);
           if(this.router.url.indexOf('document/uploaded')>0 && (this.lastFileOpenerTime==0)){
             this.lastFileOpenerTime = date.getTime();
             this.openUploaderWindow();           
           }
          break;
        default:
          console.log(selected);
      }
    }

}
