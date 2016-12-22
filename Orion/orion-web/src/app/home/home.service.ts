import { Injectable,Inject,isDevMode } from '@angular/core';
import { Http,Headers,RequestOptions } from '@angular/http';
import Utils from '../service/utility';

import 'rxjs/add/operator/map';


@Injectable()
export class HomeService {
  baseUrl; 


  constructor(private http: Http) {
    this.baseUrl = Utils.getBaseUrl();
  }


  getAll() {
    var url = this.baseUrl + 'api/user/summary';
    return this.http.get(url,[{ withCredentials: true }])
      .map(res => res.json());
  }


  updateUserHeader(body){
    let headerContent = new Headers();
    headerContent.append("Content-Type", "application/json");
    return this.http.put(this.baseUrl + 'api/user/homeHeaders', body, { headers: headerContent })
            .map(res => res.json());
  }

  
  
}