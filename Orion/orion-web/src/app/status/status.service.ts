import { Injectable,Inject,isDevMode } from '@angular/core';
import { Http,Headers,RequestOptions } from '@angular/http';
import Utils from '../service/utility';

import 'rxjs/add/operator/map';


@Injectable()
export class StatusService {
  baseUrl; 


  constructor(private http: Http) {
    this.baseUrl = Utils.getBaseUrl();
  }


  getDocStatus(orderId) {
    var url = this.baseUrl + 'api/user/status/document/' + orderId;
    return this.http.get(url,[{ withCredentials: true }])
      .map(res => res.json());
  }

  getPaymentStatus(orderId) {
    var url = this.baseUrl + 'api/user/status/payment/' + orderId;
    return this.http.get(url,[{ withCredentials: true }])
      .map(res => res.json());
  }

  getOrderStatus(orderId) {
    var url = this.baseUrl + 'api/user/status/order/' + orderId;
    return this.http.get(url,[{ withCredentials: true }])
      .map(res => res.json());
  }


  
}