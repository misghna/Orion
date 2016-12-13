import { Injectable,Inject,isDevMode } from '@angular/core';
import { Http,Headers,RequestOptions } from '@angular/http';
import Utils from '../service/utility';

import 'rxjs/add/operator/map';


@Injectable()
export class BidService {
  baseUrl; 


  constructor(private http: Http) {
    this.baseUrl = Utils.getBaseUrl();
  }


  getBid(orderRefId) {
    var url = this.baseUrl + 'api/user/bid/' + orderRefId;
    return this.http.get(url,[{ withCredentials: true }])
      .map(res => res.json());
  }

  add(body){
    let headerContent = new Headers();
    headerContent.append("Content-Type", "application/json");
    return this.http.post(this.baseUrl + 'api/user/bid', body, { headers: headerContent })
            .map(res => res.json());
  }

  update(body){
    let headerContent = new Headers();
    headerContent.append("Content-Type", "application/json");
    return this.http.put(this.baseUrl + 'api/user/bid', body, { headers: headerContent })
            .map(res => res.json());
  }
 
  deleteById(id) {
    return this.http.delete(this.baseUrl + 'api/user/bid/' + id)
      .map(res => res.json());
  }

 selectBidder(id){
    let headerContent = new Headers();
    headerContent.append("Content-Type", "application/json");
    return this.http.put(this.baseUrl + 'api/user/bid/select/' + id, null, { headers: headerContent })
            .map(res => res.json());
  }

 diselectSupplier(id){
    let headerContent = new Headers();
    headerContent.append("Content-Type", "application/json");
    return this.http.put(this.baseUrl + 'api/user/bid/diselect/' + id, null, { headers: headerContent })
            .map(res => res.json());
  }

  reqApproval(id){
    let headerContent = new Headers();
    headerContent.append("Content-Type", "application/json");
    return this.http.put(this.baseUrl + 'api/user/bid/reqApproval/' + id, null, { headers: headerContent })
            .map(res => res.json());
  }
  
  
}