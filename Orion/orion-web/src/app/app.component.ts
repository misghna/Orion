import { Component,Input,ElementRef,ViewChild,AfterViewInit,Renderer,OnInit} from '@angular/core';
import { Router } from '@angular/router';
import { Http,Headers } from '@angular/http';
import { UtilService } from './service/util.service';
import {Observable} from 'rxjs/Observable';
import { UserService } from './users/users.service';

declare var $: any;

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})

export class AppComponent implements OnInit{


  loaderHidden;

  constructor(public utilService:UtilService,private rd: Renderer,
              private userService:UserService,public router:Router) {   
    this.loaderHidden =true;
     utilService.currentLoaderState$.subscribe(
      loadState => {
        if(this.loaderHidden == loadState)  {
          this.loaderHidden = !loadState;
        }
    });

    
  }

ngOnInit() {
     var url = window.location.href;
    if(url.indexOf("open")<0){
      this.userService.getUser()
          .subscribe(
              response => {   
                    localStorage.setItem('accessDetail', JSON.stringify(response));
                    localStorage.setItem('sid',"JSESSIONID=" + response['sId']);
                    localStorage.setItem('homeHeaders',response['homeHeaders']);
                    if(response['role'] =='Admin'){
                      this.utilService.setAdminState(true);
                    }
              },
              error => {
 
                return {};
              }
            );
    }
}


}
