import { Injectable ,isDevMode} from '@angular/core';
import { Router } from '@angular/router';
import {Observable} from 'rxjs/Observable';
import { Subject }    from 'rxjs/Subject';

@Injectable()
export class UtilService {

  contId=0;

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

  private delItem = new Subject<boolean>();
  currentdelItem$ = this.delItem.asObservable();

  private docList = new Subject<boolean>();
  currentDocList$ = this.docList.asObservable();
  

  
  

constructor(public router: Router) {
 this.setLoaderState(true);
}

    redirectToLogin(){
       window.location.replace("/");
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

    
    setDocList(docs){
      this.docList.next(docs);
    }

    setSearchTxt(txt){
      this.searchTxt.next(txt);
    }

    deleteItem(param){
      this.delItem.next(param);
    }

    getErrorMsg(error){
        var msg = JSON.stringify(error);
        if (msg.indexOf('*$Start$*')>0){
             return msg.split('*$Start$*')[1].split('*$End$*')[0];
        }else{
          return "";
        }
    }


    setDocInstId(id){
      if(this.contId==0){
          this.contId =id;
      }
    }

    getDocInstId(){ return this.contId}

    search(searchObj,responseData,headers){
      if(searchObj.searchTxt==null || searchObj.searchTxt==''){       
        return responseData; 
      }

      var searchResult = [];
      var searchStrs = searchObj.searchTxt.split(" ");
      searchStrs.forEach(txt => {
          searchResult = [];
          responseData.forEach(el => {
              var rowAdded :boolean = false;
              headers.forEach(sel => {
              if(!rowAdded && el[sel.value]!=null && el[sel.value].toString().toLowerCase().indexOf(txt.toLowerCase()) !== -1){
                searchResult.push(el);
                rowAdded = true;
              }
            });  
          });
          responseData=searchResult;
      });

      return searchResult;
    }

} 