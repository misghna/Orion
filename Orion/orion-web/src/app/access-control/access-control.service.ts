import { Injectable,Inject,isDevMode } from '@angular/core';
import { Http,Headers,RequestOptions } from '@angular/http';
import Utils from '../service/utility';

import 'rxjs/add/operator/map';


@Injectable()
export class AccessControlService {
  baseUrl; 


  constructor(private http: Http) {
    this.baseUrl = Utils.getBaseUrl();
  }



  getAccessHistory(name) {
    var url = this.baseUrl + 'api/user/access/' + name;
    return this.http.get(url,[{ withCredentials: true }])
      .map(res => res.json());
  }






  
  
}