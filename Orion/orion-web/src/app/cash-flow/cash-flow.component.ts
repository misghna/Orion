import { Component, OnInit } from '@angular/core';
import { CashFlowService } from '../cash-flow/cash-flow.service';
import { MiscSettingService } from '../misc/misc-service.service';


@Component({
  selector: 'app-cash-flow',
  templateUrl: './cash-flow.component.html',
  styleUrls: ['./cash-flow.component.css']
})
export class CashFlowComponent implements OnInit {

  cFlow ={};ports=[];

  constructor(private cashFlow : CashFlowService, private miscSettingService :MiscSettingService){

  }

  ngOnInit(){
    this.cFlow['year']= new Date().getFullYear();
    this.cFlow['format']="Monthly";
    this.cFlow['currency']="USD";
    this.cFlow['dest']="All";

    this.getCashFlow();
    this.getPorts();
    
  }
    // lineChart

  public lineChartData:Array<any> = [
    {data: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0], label: 'Estimated'},
    {data: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0], label: 'Actual'},
    {data: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0], label: 'ItemCost'}
  ];

  public lineChartOptions = {
           scales: {
            yAxes: [{
                ticks: {
                    beginAtZero :true,
                    suggestedMax : 1000,
                    callback: function(value, index, values) {
                        return value.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
                    }
                }
            }]
        }
};

  public lineChartLabels:Array<any> = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul','Aug','Sep','Oct','Nov','Dec'];
  public lineChartType:string = 'line';
  public pieChartType:string = 'pie';
 
  // Pie
  public pieChartLabels:string[] = [];
  public pieChartData:number[] = [];
 
 
  public chartClicked(e:any):void {
    this.lineChartType = this.lineChartType === 'line' ? 'bar' : 'line';
    console.log(e);
  }
 
  public chartHovered(e:any):void {
    console.log(e);
  }


  getPorts() {
     this.miscSettingService.getPorts()
      .subscribe(
          response => {
              this.ports = response; 
          },
          error => {
               console.error("Something went wrong");        
          }
        );
  } 

  getCashFlow(){
      this.cashFlow.getCashFlow(this.cFlow)
      .subscribe(
          response => {
              this.lineChartData = response;
          },
          error => {
            console.error("Something went wrong");
          }
        );
  }

}