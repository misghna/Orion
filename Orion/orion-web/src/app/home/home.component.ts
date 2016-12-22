import { Component, OnInit,ElementRef } from '@angular/core';
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

  optionsList;data;selectedHeaders =[];allHeaders;

  constructor(private utilService:UtilService, private http:Http,
               private el: ElementRef,private homeService : HomeService) {  

        this.optionsList = [{'name':'Add/Remove Column','value':'addRemoveCol'}];

        this.utilService.setToolsContent(this.optionsList);

        utilService.currentToolsOptCont$.subscribe(
          opt => {  
            jQuery('#mySidenav').width(150);
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
  

  getAllData(){

    this.homeService.getAll()
      .subscribe(
          response => {
              this.generateHeaders(response);
              this.data = response; 
          },
          error => {
          //  this.popAlert("Info","danger","Something went wrong when load item list, please try again later!");          
          }
        );
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
        if(localStorage.getItem('homeHeaders')!=null){
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
     this.getAllData();
  }




}
