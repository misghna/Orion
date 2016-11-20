import { Component, OnInit } from '@angular/core';
import { MiscService } from '../service/misc.service';
import { FilterNamePipe } from '../pipes/pipe.filterName';


@Component({
  selector: 'app-items',
  templateUrl: './items.component.html',
  styleUrls: ['./items.component.css']
})
export class ItemsComponent implements OnInit {
  data;itemMsg;
  alertType;alertHidden;alertLabel;
  hideAddNewForm;  hideLoader;
  itemDeleteId;
  itemDetail={};
  taskType = "Add";
  activeItemId;
  activeProductHeader;

  filterQuery = "";

  constructor(private miscService:MiscService) {
    this.hideLoader=true;
    this.alertHidden=true;  
    this.hideAddNewForm=true;
   }

  ngOnInit() {
  }


  loadItemsTable(){
      this.miscService.getAllItems()
      .subscribe(
          response => {
              this.data = response;     
          },
          error => {
            this.popAlert("Error","danger","Something went wrong when load item list, please try again later!");          
          }
        );
    }


    addItem(event,product,hsCode,financialServices,consumerTax,stampTax,fees,others){
      event.preventDefault();
      
      var body = {"product":product,"hsCode":hsCode,"financialServices":financialServices,
                  "consumerTax":consumerTax,"stampTax":stampTax,"fees":fees,"others":others}
      
      if(this.taskType == "Update"){
            body["id"] = this.activeItemId;
            this.miscService.updateItem(body)
            .subscribe(
                response => {
                    this.popAlert("Info","success","Item successfully updated!");
                    this.data = response;     
                },
                error => {      
                    this.popAlert("Error","danger","Something went wrong, please try again later!");
                }
              );

        }else{
            this.taskType = "Add";
            this.miscService.addItem(body)
            .subscribe(
                response => {
                    this.popAlert("Info","success","Item successfully added!");
                    this.data = response;     
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

    showAddForm(){
      this.itemDetail={};
      this.taskType = "Add";
      this.hideAddNewForm=false;
    }

    popAlert(type,label,msg){
          this.alertHidden = false;
          this.alertType = type;
          this.alertLabel = label;
          this.itemMsg= msg;
    }

    deleteItem(){
      var id = this.activeProductHeader.split('-')[0];
      console.log("deleting ... " + id);
      this.miscService.deleteItem(id)
      .subscribe(
          response => {
              this.data = response;   
              this.popAlert("Info","success","Item successfully deteled!"); 
          },
          error => {
              this.popAlert("Error","danger","Something went wrong, please try again later!");
          }
        );
    }

    editItem(idd){    
      var id = idd.split('-')[0];
      this.activeItemId=id;
      this.taskType = "Update";
      this.hideAddNewForm=false;
      this.miscService.getItem(id)
      .subscribe(
          response => {
              this.itemDetail = response;     
          },
          error => {
              this.popAlert("Error","danger","Something went wrong, please try again later!");
          }
        );
    }

    search(searchObj){
      this.miscService.searchItem(searchObj)
      .subscribe(
          response => {
              this.data = response;     
          },
          error => {
              console.log(error);
              this.popAlert("Error","danger","Something went wrong, please try again later!");
          }
        );
    }
}
