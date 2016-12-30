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
    var browser = this.getBrowser().split(" ");   
    if(browser[0]=="Chrome" && browser[1] < 55){
      alert("For optimal performance, please use latest Chrome or Firefox");
    }
  
    if(browser[0]=="Firefox" && browser[1] < 50){
       window.alert("For optimal performance, please use latest Chrome or Firefox");
    }

    var url = window.location.href;
    if(url.indexOf("open")<0){
      this.userService.getUser()
          .subscribe(
              response => {   
                    localStorage.setItem('accessDetail', JSON.stringify(response));
                    localStorage.setItem('sid',"JSESSIONID=" + response['sId']);
                    localStorage.setItem('homeHeaders',response['homeHeaders']);
                    localStorage.setItem('homeColor',response['homeColor']);                   
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

getBrowser(){
    var ua= navigator.userAgent, tem,
    M= ua.match(/(opera|chrome|safari|firefox|msie|trident(?=\/))\/?\s*(\d+)/i) || [];
    if(/trident/i.test(M[1])){
        tem=  /\brv[ :]+(\d+)/g.exec(ua) || [];
        return 'IE '+(tem[1] || '');
    }
    if(M[1]=== 'Chrome'){
        tem= ua.match(/\b(OPR|Edge)\/(\d+)/);
        if(tem!= null) return tem.slice(1).join(' ').replace('OPR', 'Opera');
    }
    M= M[2]? [M[1], M[2]]: [navigator.appName, navigator.appVersion, '-?'];
    if((tem= ua.match(/version\/(\d+)/i))!= null) M.splice(1, 1, tem[1]);
    return M.join(' ');
};


}
