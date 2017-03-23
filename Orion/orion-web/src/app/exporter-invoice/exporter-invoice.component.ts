import { Component, OnInit,ElementRef,Renderer,ViewChild} from '@angular/core';
import { InvoiceFormatService } from '../invoice-format/invoice-format.service';
import { OrdersService } from '../orders/orders.service';
import { AddressBookService } from '../address-book/address-book.service';

declare var $:any;

@Component({
  selector: 'app-exporter-invoice',
  templateUrl: './exporter-invoice.component.html',
  styleUrls: ['./exporter-invoice.component.css']
})
export class ExporterInvoiceComponent implements OnInit {
 @ViewChild('invoiceHtml') input;

  alertObj={};invoice={};orders=[];formats=[];allOrdersResponse=[];allOrders=[];addresses=[];
  notLoaded=true;
  constructor(private invFrmtService :InvoiceFormatService,private el: ElementRef,private rd: Renderer,
              private orderService :OrdersService, private addressBookService : AddressBookService) {
    this.alertObj['alertHidden']=true;
    this.invoice['type'] = "Select Invoice Type"
    this.invoice['exporter'] = "Select Exporter"
    this.notLoaded=true;
   }

  ngOnInit() {
        $("#editor").hide();
        $("#slider").slider('setValue', 8);
        $("#invoiceHtml").width("80%");
        // this.preview(20);


        $("#slider").slider()
          .on('slide', function(ev){
            var divWidth=ev.value*10;
            $("#invoiceHtml").width(divWidth + "%");
        });

        var el;
        $("#invoiceHtml").on("click",function(event){
          var leftpx = $("#invoiceHtml").offset().left; 
          $("#editor").css({top: event.clientY, left: event.clientX-leftpx});
          $("#editArea").val(event.toElement.innerText);               
          el = event;
          $("#editor").show();
        })

        $("#saveBtn").on("click",function(ev){
            el.toElement.innerText = $("#editArea").val();
            $("#editor").hide();
            $("#editArea").text("");
            el=ev;
        });

        $("#cancelBtn").on("click",function(ev){
           $("#editor").hide();
        });

    //    this.loadFormats();
        this.getAllOrders();
        this.getAddressByType("Exporter");
        

  }

  save(){
    var htmlContent = this.input.nativeElement.innerHTML;
    var body = {"htmlContent":htmlContent,"invNo":this.invoice['invNo'],"invType":this.invoice['type']}
     this.invFrmtService.updateInvoice(body)
      .subscribe(
          response => {
               
          },
          error => {
            if(error.status==404){
               this.popAlert("Info","Info","invoice format not found, please add in settings!");          
            }else{
              console.log(error.status);
               this.popAlert("Error","danger","Something went wrong, please try again later!");          
            }
          }
        );
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

  generateInvoice(){
      var param = {"invNo":this.invoice['invNo'],"exporter":this.invoice['exporter'],"invType":this.invoice['type']}
      this.invFrmtService.createInvoice(param)
      .subscribe(
          response => {
                this.notLoaded=false;
               $("#invoiceHtml").html(response['_body']); 
          },
          error => {
            if(error.status==404){
               this.popAlert("Info","Info","invoice format not found, please add in settings!");          
            }else{
              console.log();
               this.popAlert("Error","danger","Something went wrong, please try again later!");          
            }
          }
        );
  }


    openInvoice(){
     var param = {"invNo":this.invoice['invNo'],"exporter":this.invoice['exporter'],"invType":this.invoice['type']}
      this.invFrmtService.getInvoice(param)
      .subscribe(
          response => {
               this.notLoaded=false;
               $("#invoiceHtml").html(response['_body']); 
          },
          error => {
            if(error.status==404){
               this.popAlert("Info","Info","invoice not found, please creat new!");          
            }else{
              console.log();
               this.popAlert("Error","danger","Something went wrong, please try again later!");          
            }
          }
        );
  }

  loadFormats(){
     this.invFrmtService.get()
      .subscribe(
          response => {
              this.formats = response;    
          },
          error => {
            console.log("exporters not found");
          }
        );
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


//  preview(id){
//     this.invFrmtService.getInvoice(id)
//       .subscribe(
//           response => {
//                $("#invoiceHtml").html(response['_body']); 
//           },
//           error => {
//             if(error.status==404){
//                this.popAlert("Info","Info","Preview not found!");          
//             }else{
//               console.log();
//                this.popAlert("Error","danger","Something went wrong, please try again later!");          
//             }
//           }
//         );
//   }





    popAlert(type,label,msg){
          this.alertObj['alertHidden'] = false;
          this.alertObj['alertType'] = type;
          this.alertObj['alertLabel'] = label;
          this.alertObj['itemMsg']= msg;
    }

}
