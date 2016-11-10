import { Component,Input,ElementRef } from '@angular/core';
import { Router } from '@angular/router';
import { Http,Headers } from '@angular/http';
import { UtilService } from './service/util.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})

export class AppComponent {
  isNavBarHidden = true;

  // constructor(public utilService:UtilService,private elementRef: ElementRef) {   
  //   this.utilService.isNavBarHidden=this.isNavBarHidden;
  // //  console.log(myGlobals.sep);
  //      console.log(elementRef.nativeElement)

  //    //  this.utilService.isNavBarHidden.s
  // }


}
