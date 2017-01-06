import { Component, OnInit,ElementRef,Renderer,ViewChild } from '@angular/core';
import { Http} from '@angular/http';
import { UtilService } from '../service/util.service';
import Utils from '../service/utility';
import {Observable} from 'rxjs/Rx';
import { HomeService } from './home.service';


declare var jQuery : any;

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})



export class HomeComponent implements OnInit {

 @ViewChild('myModalBtn2') modalInput:ElementRef;

  optionsList;data;selectedHeaders =[];allHeaders;colorSettings =[];
  newOrders;inTransitOrders;inPortOrders; inTerminalOrders;listType ="active";responseData;
 
  colors = ['silver','gray','Black','Red','maroon','yellow','Olive','Lime','Green','Aqua','Teal','Blue','Navy','Fuchsia','Purple','White'];
  
  coloredColumns = [{'name':'Base Size','value':'baseSize'},{'value':'qtyPerPack','name':'Qty Per Pack'},
                    {'value':'pclPerCont','name':'Pck Per Cont'},{'value':'contSize','name':'Cont Size'},
                    {'value':'contQnt','name':'Cont Qty'}];

  colorConfig = {'colValue':'Select','colName':'Select','bckColor':'Select','txtColor':'Select','less':'','greater':''};

  constructor(private utilService:UtilService, private http:Http,
               private el: ElementRef,private homeService : HomeService,private rd: Renderer) {  

        this.optionsList = [{'name':'Add/Remove Column','value':'addRemoveCol'},
                            {'name':'Edit notification color','value':'notifColor'},
                            {'name':'Show cleared Orders','value':'showCleared'}];

        this.utilService.setToolsContent(this.optionsList);
        utilService.currentSearchTxt$.subscribe(txt => {this.search(txt);});
        utilService.currentToolsOptCont$.subscribe(
          opt => {  
            if(opt['optionName'] == 'addRemoveCol'){
              jQuery('#mySidenav').width(150);
            }else if(opt['optionName'] == 'notifColor'){
               let event = new MouseEvent('click', {bubbles: true});
                this.rd.invokeElementMethod(
                this.modalInput.nativeElement, 'dispatchEvent', [event]);
            }else if(opt['optionName'] == 'showCleared'){
                this.listType = "all";
                this.getAllData();
            }
        });

        jQuery(this.el.nativeElement).on('click','#sideMenuClose',function(){
          jQuery('#mySidenav').width(0);
        });
      
        // polling 
        // Observable.interval(300000)
        //   .take(10).map((x) => x+1)
        //   .subscribe((x) => {
        //     this.getAllData();
        //   });

  }
  



  addColorSetting(){
    for(var key in this.colorConfig){
      if(this.colorConfig[key] == 'Select' || this.colorConfig[key] == ''){
        alert("Set all fields and try again");
        return;
      }
    }
    if(this.colorSettings==null)this.colorSettings=[];
    this.colorSettings.push(this.colorConfig);
    this.homeService.updateHomeColor(JSON.stringify(this.colorSettings))
      .subscribe(
          response => {
               
          },
          error => {
            alert("error when adding new settings");         
          }
        );
  }

  getAllData(){
    this.homeService.getAll(this.listType)
      .subscribe(
          response => {
              this.generateHeaders(response);
              this.data = this.addColor(response);
              this.responseData= this.data;
          },
          error => {
          //  this.popAlert("Info","danger","Something went wrong when load item list, please try again later!");          
          }
        );
  }


addColor(data){
   var newData = []
    data.forEach(item => {
       item['txtColor']= this.setTxtColor(item);
       item['bckColor']= this.setBckGraColor(item);
       newData.push(item);
    });
    return newData;
}

setTxtColor(item){
   var txtColor;
    this.colorSettings.forEach(color => {
        if(item[color.colValue] > color.greater && item[color.colValue]< color.less){        
          txtColor= color.txtColor;
        }
    });
    return txtColor;
}

setBckGraColor(item){
    var bckColor;
    this.colorSettings.forEach(color => {
        if(item[color.colValue]>color.greater && item[color.colValue]< color.less){
          bckColor= color.bckColor;
        }
    });
    return bckColor;
}

deleteColor(colorSet){
  var i=-1;
  for(var j=0; j<this.colorSettings.length; j++){
    if(JSON.stringify(this.colorSettings[j])==JSON.stringify(colorSet)){
      i=j;break;
    }
  }

if(i>-1){
  this.colorSettings.splice(i,1);
  this.homeService.updateHomeColor(this.colorSettings)
     .subscribe(
          response => {
              localStorage.setItem('homeColor',JSON.stringify(this.colorSettings));
          },
          error => {
              alert("error when adding new settings");            
            }
        );
}

}
  generateHeaders(response){
      var headArray =[];
      if(response.length>0){
        var rex = /([A-Z])([A-Z])([a-z])|([a-z])([A-Z])/g;
        var rexCap = /\b[a-z0-9-_]+/i;
        var f = response[0];
        var name :any;
        for (name in f) {
          if(name != 'id'){
             var hdrs = name.replace( rex, '$1$4 $2$3$5' );              
             var hdrs2 = hdrs.toLowerCase().replace(/([^a-z])([a-z])(?=[a-z]{2})|^([a-z])/g, function(_, g1, g2, g3) {
                return (typeof g1 === 'undefined') ? g3.toUpperCase() : g1 + g2.toUpperCase(); } );
             headArray.push({'value' :name,'name' : hdrs2});
          }
        }
        var activeHeaderVals;
        if(localStorage.getItem('homeHeaders')!=null || localStorage.getItem('homeHeaders')!='undefined'){
          activeHeaderVals = JSON.parse(localStorage.getItem('homeHeaders'));
        }else{
          activeHeaderVals = ['item','brand','contSize','contQty'];
        }

        var selHeaders = [];
        headArray.forEach(element => {
            if(activeHeaderVals.indexOf(element.value)>-1){
              selHeaders.push(element);
              element.selected=true;
            }else{
              element.selected=false;
            }

        });
        this.selectedHeaders = selHeaders;
        this.allHeaders = headArray;
      }

  }


sorter(a, b){
     var A = a.toLowerCase();
     var B = b.toLowerCase();
     if (A < B){
        return -1;
     }else if (A > B){
       return  1;
     }else{
       return 0;
     }
}

  applyColSelection(event){
    event.preventDefault();
    var hdList=[];
    this.allHeaders.filter(_ => _.selected).forEach(sel => { 
      hdList.push(sel.value);
    });

     this.homeService.updateUserHeader(JSON.stringify(hdList))
      .subscribe(
          response => {
              jQuery('#mySidenav').width(0);
              localStorage.setItem('homeHeaders',JSON.stringify(hdList));
              this.getAllData();
          },
          error => {
          //  this.popAlert("Info","danger","Something went wrong when load item list, please try again later!");          
          }
        );
  }


  ngOnInit() {
      if(localStorage.getItem('homeColor')!='undefined'){
        this.colorSettings = JSON.parse(localStorage.getItem('homeColor'));
      }
     this.getAllData();
     this.getOrderStat();
  }

 getOrderStat(){
   // get new orders
     this.homeService.getNewOrder()
      .subscribe(response => {
            this.newOrders= response.length;
          },
          error => {  } );

    // get In Transit orders
     this.homeService.getInTransit()
      .subscribe(response => {
            this.inTransitOrders= response.length;
          },
          error => {  } );

   // get In Port orders
     this.homeService.getInPort()
      .subscribe(response => {
            this.inPortOrders= response.length;
          },
          error => {  } );       

   // get In Terminal orders
     this.homeService.getInTerminal()
      .subscribe(response => {
            this.inTerminalOrders= response.length;
          },
          error => {  } );    
 }
   

    search(searchObj){
      if(searchObj.searchTxt==null || searchObj.searchTxt==''){       
        this.data = this.responseData; 
        return;
      }

      var searchResult = [];
      this.responseData.forEach(el => {
          var rowAdded :boolean = false;
          this.selectedHeaders.forEach(sel => {
          if(!rowAdded && el[sel.value]!=null && el[sel.value].toString().toLowerCase().indexOf(searchObj.searchTxt.toLowerCase()) !== -1){
            searchResult.push(el);
            rowAdded = true;
          }
        });  
      });
      this.data = searchResult;

    }


    // proccessSearch(el,searchTxt){
    //   this.selectedHeaders.forEach(sel => {
    //       if(el[sel.value]!=null && el[sel.value].toString().toLowerCase().indexOf(searchTxt.toLowerCase()) !== -1){
    //         console.log(el[sel.value]);
    //         return true;
    //       }
    //   });        
    //   return false;
    // }

}
