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


  getAll(state) {
    var url = this.baseUrl + 'api/user/summary/' + state;
    return this.http.get(url,[{ withCredentials: true }])
      .map(res => res.json());
  }

  getNewOrder() {
    var url = this.baseUrl + 'api/user/newOrder';
    return this.http.get(url,[{ withCredentials: true }])
      .map(res => res.json());
  }

  getInTransit() {
    var url = this.baseUrl + 'api/user/inTransit';
    return this.http.get(url,[{ withCredentials: true }])
      .map(res => res.json());
  }

  getInPort() {
    var url = this.baseUrl + 'api/user/inPort';
    return this.http.get(url,[{ withCredentials: true }])
      .map(res => res.json());
  }

  getInTerminal() {
    var url = this.baseUrl + 'api/user/inTerminal';
    return this.http.get(url,[{ withCredentials: true }])
      .map(res => res.json());
  }

  updateUserHeader(body){
    let headerContent = new Headers();
    headerContent.append("Content-Type", "application/json");
    return this.http.put(this.baseUrl + 'api/user/homeHeaders', body, { headers: headerContent })
            .map(res => res.json());
  }

  updateHomeColor(body){
    let headerContent = new Headers();
    headerContent.append("Content-Type", "application/json");
    return this.http.put(this.baseUrl + 'api/user/homeColor', body, { headers: headerContent })
            .map(res => res.json());
  }
  
  
}