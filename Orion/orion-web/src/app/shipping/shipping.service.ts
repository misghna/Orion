import { Injectable,Inject,isDevMode } from '@angular/core';
import { Http,Headers,RequestOptions } from '@angular/http';
import Utils from '../service/utility';

import 'rxjs/add/operator/map';


@Injectable()
export class ShippingService {
  baseUrl; 


  constructor(private http: Http) {
    this.baseUrl = Utils.getBaseUrl();
  }


  get(orderRefId) {
    var url = this.baseUrl + 'api/user/ship/' + orderRefId;
    return this.http.get(url,[{ withCredentials: true }])
      .map(res => res.json());
  }


  trackShipping(bl) {
    var url = this.baseUrl + 'api/user/ship/track/' + bl;
    return this.http.get(url,[{ withCredentials: true }]);
  }


  getAllAgency() {
    var url = this.baseUrl + 'api/user/agency';
    return this.http.get(url,[{ withCredentials: true }])
      .map(res => res.json());
  }

  add(body,state){
    let headerContent = new Headers();
    headerContent.append("Content-Type", "application/json");
    return this.http.post(this.baseUrl + 'api/user/ship/'+ state, body, { headers: headerContent })
            .map(res => res.json());
  }

  update(body,state){
    let headerContent = new Headers();
    headerContent.append("Content-Type", "application/json");
    return this.http.put(this.baseUrl + 'api/user/ship/' + state, body, { headers: headerContent })
            .map(res => res.json());
  }
 
  deleteById(id,state) {
    return this.http.delete(this.baseUrl + 'api/user/ship/' + id + "/" + state)
      .map(res => res.json());
  }

  
  
}