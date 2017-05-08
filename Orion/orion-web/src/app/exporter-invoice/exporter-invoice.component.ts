import { Component, OnInit,ElementRef,Renderer,ViewChild} from '@angular/core';
import { InvoiceFormatService } from '../invoice-format/invoice-format.service';
import { OrdersService } from '../orders/orders.service';
import { AddressBookService } from '../address-book/address-book.service';
import { UtilService } from '../service/util.service';

declare var $:any;

@Component({
  selector: 'app-exporter-invoice',
  templateUrl: './exporter-invoice.component.html',
  styleUrls: ['./exporter-invoice.component.css']
})
export class ExporterInvoiceComponent implements OnInit {
 @ViewChild('invoiceHtml') input;
 @ViewChild('invoiceHtml') dataContainer: ElementRef;

  alertObj={};invoice={};orders=[];formats=[];allOrdersResponse=[];allOrders=[];addresses=[];
  notLoaded=true;needsApproval=false;delta="TestPlug";invoiceHtmlContent="";
  constructor(private invFrmtService :InvoiceFormatService,private el: ElementRef,private rd: Renderer,
              private orderService :OrdersService, private addressBookService : AddressBookService,
              private utilService :UtilService) {

    this.alertObj['alertHidden']=true;
    this.invoice['type'] = "Select Invoice Type"
    this.invoice['exporter'] = "Select Exporter"
    this.notLoaded=true;
    this.delta="TestPlug";
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
        this.invoice['font']='Courier New'

  }

  generatePdf(){
    var htmlContent = this.input.nativeElement.innerHTML;
    var body = {"invoice":htmlContent,"invNo":this.invoice['invNo'],"invoiceType":this.invoice['type']}
     this.invFrmtService.generatePdf(body)
      .subscribe(
          response => {
               this.popAlert("Info","Info","Invoice uploaded in PDF format, please dowload it from document section!"); 
          },
          error => {
            if(error.status==400){
               this.popAlert("Error","danger","Bad data!");          
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

   filterName(inv){
        if(inv.length<1){
          return this.allOrders;
        }
        this.allOrders = this.allOrdersResponse.filter(order => (
            (order.invNo.toLowerCase().indexOf(inv.toLowerCase()) !== -1) ));
    }

  generateInvoice(){
      var param = {"invNo":this.invoice['invNo'],"invType":this.invoice['type']}
      this.invFrmtService.createInvoice(param)
      .subscribe(
          response => {
                this.notLoaded=false;
              $("#invoiceHtml").html(response['_body']); 
              $("#invoiceHtml").find("#main").css("font-family", this.invoice['font']);
              //this.invoiceHtmlContent = response['_body'];
              //this.dataContainer.nativeElement.innerHTML = response['_body'];
          },
          error => {
            if(error.status==404){
              this.notLoaded=true;
               $("#invoiceHtml").html();
               this.popAlert("Info","Info","invoice format not found, please add in settings!");          
            }else if(error.status==400){
              this.notLoaded=true;
               $("#invoiceHtml").html();
               this.popAlert("Error","danger",this.utilService.getErrorMsg(error));          
            }else{
              console.log();
               this.popAlert("Error","danger","Something went wrong, please try again later!");          
            }
          }
        );
  }


updateFont(font){
  $("#invoiceHtml").find("#main").css("font-family", font);
  this.invoice['font'] = font;
  this.notLoaded = true;
}
  //   openInvoice(){

  //   if(this.invoice['invNo'] == null){
  //      this.popAlert("Info","Info","Please select order by Invoice No!"); 
  //      return;
  //    }
  //    if(this.invoice['type']=="Select Invoice Type"){
  //      this.popAlert("Info","Info","Please select Invoice Type!"); 
  //      return;
  //    }
  //    if(this.invoice['exporter']=="Select Exporter"){
  //      this.popAlert("Info","Info","Please select Exporter!"); 
  //      return;
  //    }
  //    var param = {"invNo":this.invoice['invNo'],"exporter":this.invoice['exporter'],"invType":this.invoice['type']}
  //     this.invFrmtService.getInvoice(param)
  //     .subscribe(
  //         response => {
  //              this.notLoaded=false;
  //             // $("#invoiceHtml").html(response['_body']); 
  //              this.invoiceHtmlContent = response['_body'];
  //         },
  //         error => {
  //           if(error.status==404){
  //              this.notLoaded=true;
  //              $("#invoiceHtml").html();
  //              this.popAlert("Info","Info","invoice not found, please creat new!");          
  //           }else{
  //             console.log();
  //              this.popAlert("Error","danger","Something went wrong, please try again later!");          
  //           }
  //         }
  //       );
  // }

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
