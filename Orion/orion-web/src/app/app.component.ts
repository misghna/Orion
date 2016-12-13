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



  loaderHidden2 =true;
  subscription;

  constructor(public utilService:UtilService,private rd: Renderer) {   

     this.subscription = utilService.currentLoaderState$.subscribe(
      mission => {        
        this.loaderHidden2 = ! mission;
    });

  }



}
