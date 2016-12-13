import { Component, OnInit,ElementRef } from '@angular/core';
import { Http} from '@angular/http';
import { UtilService } from '../service/util.service';

import Utils from '../service/utility';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})



export class HomeComponent implements OnInit {

  constructor(private utilService:UtilService, private http:Http,
               private elementRef: ElementRef) {  

                console.log(Utils.getBaseUrl());

  }
      
  ngOnInit() {
  }


   trigerSpinner(event){
     event.preventDefault();
     var modalInfo = {"msg" : " this is message","task" :"myTask"};
     this.utilService.showModalState(modalInfo);
   }

   delete(event){
     console.log(event);
   }

}
