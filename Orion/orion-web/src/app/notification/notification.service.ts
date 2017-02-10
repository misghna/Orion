import { Injectable,Inject,isDevMode } from '@angular/core';
import { Http,Headers,RequestOptions } from '@angular/http';
import Utils from '../service/utility';

import 'rxjs/add/operator/map';


@Injectable()
export class NotificationService {
  baseUrl; 


  constructor(private http: Http) {
    this.baseUrl = Utils.getBaseUrl();
  }


  getRequired(type){
    var url = this.baseUrl + 'api/user/required/'+ type
    return this.http.get(url,[{ withCredentials: true }])
      .map(res => res.json());
  }


  update(body){
    let headerContent = new Headers();
    headerContent.append("Content-Type", "application/json");
    return this.http.put(this.baseUrl + 'api/user/notification', body, { headers: headerContent })
            .map(res => res.json());
  }


 


  
}