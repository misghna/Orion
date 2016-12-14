import { Injectable ,isDevMode} from '@angular/core';
import { Router } from '@angular/router';
import {Observable} from 'rxjs/Observable';
import { Subject }    from 'rxjs/Subject';

@Injectable()
export class UtilService {

  private headerState = new Subject<boolean>();
  currentHeaderState$ = this.headerState.asObservable();

  private adminState = new Subject<boolean>();
  currentAdminState$ = this.adminState.asObservable();

  private loaderState = new Subject<boolean>();
  currentLoaderState$ = this.loaderState.asObservable();

  private modalState = new Subject<any>();
  currentModalState$ = this.modalState.asObservable();

  private toolsCont = new Subject<boolean>();
  currentToolsCont$ = this.toolsCont.asObservable();

  private toolsOpt = new Subject<boolean>();
  currentToolsOptCont$ = this.toolsOpt.asObservable();

  private searchTxt = new Subject<boolean>();
  currentSearchTxt$ = this.searchTxt.asObservable();
  

constructor(public router: Router) {
 this.setLoaderState(true);
}

    redirectToLogin(){
      this.router.navigate(['/login']);
    }

    getBaseUrl(){
      if(isDevMode()){
        return 'http://localhost:8080/';     
      }else{
        return '';
      }
    }

    getActiveUser(){
      return localStorage.getItem('accessDetail');
    }


    setAdminState(adminState: boolean) {
       this.adminState.next(adminState);
    }

    setHeaderState(headerState: boolean) {
       this.headerState.next(headerState);
    }

    setLoaderState(loaderState: boolean) {
       this.loaderState.next(loaderState);
    }

    showModalState(modalInfo: any) {
       this.modalState.next(modalInfo);
    }

    setToolsContent(toolsObj: any) {
       this.toolsCont.next(toolsObj);
    }

    setCurrentToolsOption(option: any) {
       this.toolsOpt.next(option);
    }

    setSearchTxt(txt){
      this.searchTxt.next(txt);
    }


    getErrorMsg(error){
        var msg = JSON.stringify(error);
        if (msg.indexOf('*$Start$*')>0){
             return msg.split('*$Start$*')[1].split('*$End$*')[0];
        }else{
          return "";
        }
    }

} 