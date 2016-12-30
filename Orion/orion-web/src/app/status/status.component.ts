import { Component, OnInit } from '@angular/core';

declare var $ :any;

@Component({
  selector: 'app-status',
  templateUrl: './status.component.html',
  styleUrls: ['./status.component.css']
})
export class StatusComponent implements OnInit {

 payPercent; orderPercent; docPercent;

 payments = [{'type':'CNF','done':'yes'},{'type':'Bromangol','done':'yes'},{'type':'Legalization','done':'yes'},
            {'type':'Customs','done':'yes'},{'type':'Port','done':'no'},{'type':'Terminal','done':'no'}]

 orders = [{'type':'Order Initiated','done':'yes'},{'type':'Proforma Invoice Received','done':'yes'},{'type':'Supplier Selected','done':'yes'},
            {'type':'Commercial Invoice Received','done':'yes'},{'type':'Item Shipped','done':'no'}]

 documents = [{'type':'Proforma Invoice','done':'yes'},{'type':'Commerical Invoice','done':'yes'},
              {'type':'Bill of Loading','done':'yes'},{'type':'CNCA','done':'yes'},
            {'type':'Certificate of Health','done':'yes'},{'type':'Certificate of Origin','done':'no'},
            {'type':'Certificate of Analise','done':'yes'},{'type':'Certificate of Fumigation','done':'nrlv'},
            {'type':'Certificate of Quality','done':'yes'},{'type':'Certificate of Insurance','done':'nrlv'},
            {'type':'Du License','done':'yes'},{'type':'Inspection','done':'no'},
            {'type':'Local Phytosanitary','done':'yes'},{'type':'Packing List','done':'no'}]

 itemDetail = {'bl' :'test bl', 'invNo' : 'test inv no'};
  constructor() { }

  ngOnInit() {

    this.payPercent = this.calcPercent(this.payments);
    this.orderPercent = this.calcPercent(this.orders);
    this.docPercent = this.calcPercent(this.documents);
 
    this.overAllProgress();



  }


overAllProgress(){
  var mrg1 = JSON.parse(JSON.stringify(this.payments));
  Array.prototype.push.apply(mrg1,this.orders);
  Array.prototype.push.apply(mrg1,this.documents);

    var averAllProgress = this.calcPercent(mrg1);

    $('.demo').kumaGauge({
      value : averAllProgress,
      radius : 140,
      showNeedle : false,
      min : 0,
      max: 100,
      valueLabel : {
        display : true, 
        fontFamily : 'Arial', 
        fontColor : '#000', 
        fontSize : 20, 
        fontWeight : 'normal',
        value:80
      },
      title : {
        display : true, 
        value : 'Over all progress', 
        fontFamily : 'Arial', 
        fontColor : '#818181', 
        fontSize : 20, 
        fontWeight : 'normal'
      }
    });

}

calcPercent(status){
  var count=0; var nrlv=0;
  status.forEach(element => {
    if(element['done']=='yes'){
      count = count +1;
    }
    if(element['done']=='nrlv'){
      nrlv = nrlv +1;
    }
  });

  return Math.round(count/(status.length-nrlv)*100);
}

}



