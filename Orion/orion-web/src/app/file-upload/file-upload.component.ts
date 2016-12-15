import { Component, OnInit,ElementRef } from '@angular/core';
import { FileUploader } from 'ng2-file-upload';
import { UtilService } from '../service/util.service';

declare var $ :any;

//const URL = 'https://evening-anchorage-3159.herokuapp.com/api/';
const URL = 'http://localhost:8080/uploadFile/';

@Component({
  selector: 'app-file-upload',
  templateUrl: './file-upload.component.html',
  styleUrls: ['./file-upload.component.css']
})
export class FileUploadComponent implements OnInit {

  optionsList;fData=[]; alertWin={};
  docTypes;uploadType;
  constructor(private el : ElementRef,private utilService: UtilService){
    this.alertWin = {'alertHidden':true,'type':'','label':'','msg':''}
    this.optionsList = [{'name':'Upload Document','value':'addNew'}];
    this.docTypes = ['Proforma invoice','Commercial Invoice','Bill of Loading','Other'];
    this.utilService.setToolsContent(this.optionsList);
    
    utilService.currentToolsOptCont$.subscribe(
      opt => {  
        this.option(opt);
    });
  }

  public uploader:FileUploader = new FileUploader({url: URL});
  public hasBaseDropZoneOver:boolean = false;
  public hasAnotherDropZoneOver:boolean = false;

  public fileOverBase(e:any):void {
    this.hasBaseDropZoneOver = e;
  }

  public fileOverAnother(e:any):void {
    this.hasAnotherDropZoneOver = e;
  }

  ngOnInit() {
    // console.log(this.uploader.);
    this.uploader.onBuildItemForm = (fileItem: any, form: any) => {
      var attachDoc = this.getFileDate(fileItem.file.name)
      // console.log(JSON.stringify(attachDoc));
      form.append('data', JSON.stringify(attachDoc));
      form.append('orderRef', 3344);
    };

    this.uploader.onAfterAddingFile =(fileItem: any) => {
       this.fData.push({'file':fileItem.file.name,'name' : '' , 'type' :'Select Type','remark':''});
    }

    this.uploader.onSuccessItem=(fileItem: any, response: string, status: number, headers: any)=>{
      this.popAlert("Info","success","Document(s) uploaded successfully!"); 
        setTimeout(() => {
          if(this.uploadType=='multi'){
             this.uploader.clearQueue();
          }
      }, 2000)  
    }
  }

changeType(i,type){
  this.fData[i].type = type;
}


  uploadAllFiles(event){
       event.preventDefault();
       this.uploadType='multi';
      var dataInvalid = false;
      this.fData.forEach((data, index) => {
          if(data.type=='Select Type'){
            this.popAlert("Error","Danger","select Type for all the document!"); 
            dataInvalid = true;
          }
      });
      if(!dataInvalid){
        this.uploader.uploadAll()
      }
  }

uploadOneFile(item){
    this.uploadType='single';
    var dataInvalid = false;
    this.fData.forEach((data, index) => {
        if(data.file == item.file.name && data.type=='Select Type'){
          this.popAlert("Error","Danger","select Type for the documents!"); 
          dataInvalid = true;
        }
    });
    if(!dataInvalid){
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
