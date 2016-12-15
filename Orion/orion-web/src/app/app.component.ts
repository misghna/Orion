import { Component,Input,ElementRef,ViewChild,AfterViewInit,Renderer} from '@angular/core';
import { Router } from '@angular/router';
import { Http,Headers } from '@angular/http';
import { UtilService } from './service/util.service';
import {Observable} from 'rxjs/Observable';

declare var $: any;

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})

export class AppComponent {


  loaderHidden;

  constructor(public utilService:UtilService,private rd: Renderer) {   
    this.loaderHidden =true;
     utilService.currentLoaderState$.subscribe(
      loadState => {
        console.log(loadState);
        if(this.loaderHidden == loadState)  {
          this.loaderHidden = !loadState;
        }
    });

  }



}
