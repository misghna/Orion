import { Component, OnInit,ElementRef } from '@angular/core';
import { MiscService } from '../service/misc.service';
import { FilterNamePipe } from '../pipes/pipe.filterName';
import {UtilService} from '../service/util.service';

declare var jQuery : any;

@Component({
  selector: 'app-items',
  templateUrl: './items.component.html',
  styleUrls: ['./items.component.css']
})
export class ItemsComponent implements OnInit {
  data;responseData;itemMsg;
  alertType;alertHidden;alertLabel;
  hideAddNewForm;  hideLoader;
  itemDeleteId;
  itemDetail={};
  taskType = "Add";
  activeItemId;
  activeProductHeader;
  pageName;
  optionsList;
  revision;mf;
  revisionLong;rangeSelectorHidden=true;
  bntOption="Open";revisions;

  headers = [];
  filterQuery = "";

  constructor(private miscService:MiscService,private el: ElementRef,private utilService : UtilService) {
    utilService.currentSearchTxt$.subscribe(txt => {this.search(txt);});

    this.optionsList = [{'name':'Add New Item','value':'addNew'},{'name':'Create New Revision','value':'createNewRevision'},
                                {'name':'Delete Selected Revision','value':'deleteRevision'}];
    this.utilService.setToolsContent(this.optionsList);

    // tools listener
    utilService.currentToolsOptCont$.subscribe(
      opt => {  
        this.option(opt);
    });

    utilService.currentdelItem$.subscribe(
      opt => { 
        this.delete();
    });
    
    this.pageName;
    this.hideLoader=true;
    this.alertHidden=true;  
    this.hideAddNewForm=true;
   }

  ngOnInit() {
      this.headers = [{'name':'Id','value':'id','j':'x'}, {'name':'Item Id','value':'itemId','j':'l'},{'name':'Product','value':'name','j':'l'},
                      {'name':'Brand','value':'brand','j':'l'},{'name':'Type','value':'type','j':'l'},
                      {'name':'HSCode', 'value':'hsCode','j':'c'}, {'name':'Financial Service','value':'financialServices','j':'c'},
                      {'name':'Consumer Tax','value':'consumerTax','j':'c'},{'name':'Stamp tax','value': 'stampTax','j':'c'},
                      {'name':'Fees','value':'fees','j':'c'}, {'name':'Others','value':'others','j':'c'},{'name':'Total','value':'total','j':'c'},
                       {'name':'Updated On','value':'updatedOn','j':'l'}];
      
        this.getAllRevisions();
        this.loadAll("latest");

        jQuery(this.el.nativeElement).on('click','#revisionSelector li',function(){
          jQuery('#revBtn').html('Items Revision : ' + jQuery(this).text().trim());
          jQuery('#revBtn').attr('value',jQuery(this).text().trim());           
        });
        

  }

  changeRevision(event){
    var target = event.target || event.srcElement || event.currentTarget;
    var idAttr = target.text;
    this.loadAll(target.text);
  }

  getAllRevisions(){
      this.miscService.getRevisions()
      .subscribe(
          response => {
              if(response.length>0){
                this.revisions=response;
             }
          },
          error => {
            this.popAlert("Error","danger","Something went wrong when load item list, please try again later!");          
          }
        );
  }

  execute(event,rev){
    event.preventDefault();
    this.loadAll(rev);
  }

  loadAll(rev){
    var setRev :boolean;
     this.miscService.getAllItems(rev)
      .subscribe(
          response => {
              this.setData(response);    
              this.rangeSelectorHidden = true;
          },
          error => {
            this.popAlert("Error","danger","Something went wrong, please try again later!");          
          }
        );
  }

  setData(response){
      if(response.length>0 && response[0]['revision']!=null){
      this.revisionLong = response[0]['revision'];
      this.revision = this.parseDate(response[0]['revision']);
      }
      this.responseData=response;
      this.data = response;  
  }


    addItem(event){
      event.preventDefault();
      
      if(this.taskType == "Update"){
            this.itemDetail["id"] = this.activeItemId;
            this.miscService.updateItem(this.itemDetail)
            .subscribe(
                response => {
                    this.popAlert("Info","success","Item successfully updated!");
                     this.hideAddNewForm = true; 
                    this.setData(response);                       
                },
                error => {      
                    this.popAlert("Error","danger","Something went wrong, please try again later!");
                }
              );

        }else{
            this.itemDetail['revision'] = this.revisionLong;
            this.taskType = "Add";
            this.miscService.addItem(this.itemDetail)
            .subscribe(
                response => {
                    this.popAlert("Info","success","Item successfully added!");
                    this.hideAddNewForm = true;  
                    this.setData(response);                      
                },
                error => {
                  if(error.status==400){
                    this.popAlert("Error","danger","Product Name or HSCode already exists, please check & try again!");
                  }else{
                    this.popAlert("Error","danger","Something went wrong, please try again later!");
                  }
                }
              );
        }
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

      if(dd<10) {
          var ddd = '0' + dd;
      } 

      if(mm<10) {
          var mmm = '0'+ mm
      } 
      return ddd+'/'+mmm+'/'+yyyy;
    }

    showAddForm(){
      this.taskType = "Add";
      this.hideAddNewForm=false;
      this.itemDetail={};
    }

    popAlert(type,label,msg){
          this.alertHidden = false;
          this.alertType = type;
          this.alertLabel = label;
          this.itemMsg= msg;
    }

  triggerDelModal(id){
      var modalInfo = {"title" : "Item", "msg" : id.split('-')[1],"task" :"myTask"};
      this.utilService.showModalState(modalInfo);
  }

    delete(){
      var countDash = (this.activeProductHeader.match(/-/g) || []).length;
      if(countDash==2){ // for the whole revision
        this.miscService.deleteItemByRev(this.activeProductHeader)
          .subscribe(
            response => {
                this.setData(response);  
                this.popAlert("Info","success","Item Revision successfully deteled!"); 
          },
          error => {
              this.popAlert("Error","danger","Something went wrong, please try again later!");
          }
        );
        return;
     
     }else{ // for single item
        var id = this.activeProductHeader.split('*')[0];
        this.miscService.deleteItem(id)
        .subscribe(
            response => {
                this.setData(response);  
                this.popAlert("Info","success","Item successfully deteled!"); 
            },
            error => {
                this.popAlert("Error","danger","Something went wrong, please try again later!");
            }
          );
        }

    }

    editItem(idd){        
      var id = idd.split('-')[0];
      this.activeItemId = id;
      this.taskType = "Update";
      this.itemDetail  = this.responseData.filter(item => item.id==id)[0];
      this.hideAddNewForm=false;
    }

    search(searchObj){
      this.data= this.utilService.search(searchObj,this.responseData,this.headers);
    }


    createNewRevision(rev){
        this.miscService.createNewRevision(rev)
        .subscribe(
            response => {
                this.setData(response);  
                this.getAllRevisions();
                this.popAlert("Info","success","New duplicate revision created successfully!"); 
            },
            error => {
                this.popAlert("Error","danger","Something went wrong, please try again later!");
            }
          );
    }


    option(options){
      console.log("triggered" + options);
      var selected = options.optionName;
      switch(true){
        case (selected == 'addNew') :
           this.showAddForm();
          break;
        case (selected == 'createNewRevision') :
            this.createNewRevision(this.revision);
          break;
        case (selected == 'deleteRevision') :
           this.activeProductHeader = this.revision;
            var modalInfo = {"title" : "Items", "msg" : "Revision " + this.activeProductHeader,"task" :"myTask"};
            this.utilService.showModalState(modalInfo);
           jQuery('#itemModal').modal('show');
          break;
        default:
          console.log(selected);
      }
    }

    replaceAll(string,old,newStr){
      return string.split('old').join('newStr');
    }


    validateDigits(event: any) {
      var existing = event.target.value;
      if(existing.indexOf('.')>-1 && event.charCode ==46){
        event.preventDefault();
      }
      const pattern = /^[0-9.]+$/;
      let inputChar = String.fromCharCode(event.charCode);
      if (!pattern.test(inputChar)) {
        event.preventDefault();
      }

    }


    validateNumber(event: any) {

      const pattern = /^[0-9]+$/;
      let inputChar = String.fromCharCode(event.charCode);
      if (!pattern.test(inputChar)) {
        event.preventDefault();
    }
  }

    

}
