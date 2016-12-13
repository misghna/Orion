import { Injectable,Inject,isDevMode } from '@angular/core';
import { Http,Headers,RequestOptions } from '@angular/http';
import Utils from '../service/utility';

import 'rxjs/add/operator/map';


@Injectable()
export class OrdersService {
  baseUrl; 


  constructor(private http: Http) {
    this.baseUrl = Utils.getBaseUrl(); 
  //  this.baseUrl = 'http://localhost:8080/'; 
  }


  getOrderById(orderId) {
    var url = this.baseUrl + 'api/user/order/' + orderId;
    return this.http.get(url,[{ withCredentials: true }])
      .map(res => res.json());
  }

  getOrder(year,month) {
    var url = this.baseUrl + 'api/user/order/' + year + '/' + month;
    return this.http.get(url,[{ withCredentials: true }])
      .map(res => res.json());
  }

  addOrder(body,year,month){
    let headerContent = new Headers();
    headerContent.append("Content-Type", "application/json");
    return this.http.post(this.baseUrl + 'api/admin/order/' + year + '/' + month, body, { headers: headerContent })
            .map(res => res.json());
  }

  updateOrder(body,year,month){
    let headerContent = new Headers();
    headerContent.append("Content-Type", "application/json");
    return this.http.put(this.baseUrl + 'api/admin/order/' + year + '/' + month, body, { headers: headerContent })
            .map(res => res.json());
  }

  updateItem(body){
    let headerContent = new Headers();
    headerContent.append("Content-Type", "application/json");
    return this.http.put(this.baseUrl + 'api/admin/item', body, { headers: headerContent })
            .map(res => res.json());
  }

  deleteItem(itemId) {
    return this.http.delete(this.baseUrl + 'api/admin/item/' + itemId)
      .map(res => res.json());
  }

  deleteItemByRev(rev) {
    return this.http.delete(this.baseUrl + 'api/admin/item/revision/' + rev)
      .map(res => res.json());
  }

  deleteOrderById(planId,year,month) {
    return this.http.delete(this.baseUrl + 'api/admin/order/' + planId + '/' + year + '/' + month)
      .map(res => res.json());
  }

  deleteOrder(year,month) {
    return this.http.delete(this.baseUrl + 'api/admin/order/' + year + '/' + month)
      .map(res => res.json());
  }

  
}