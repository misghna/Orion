import { Injectable,Inject,isDevMode } from '@angular/core';
import { Http,Headers,RequestOptions } from '@angular/http';
import Utils from '../service/utility';

import 'rxjs/add/operator/map';


@Injectable()
export class PortFeeService {
  baseUrl; 


  constructor(private http: Http) {
    this.baseUrl = Utils.getBaseUrl();
  }


  get(){
    var url = this.baseUrl + 'api/user/portFee'
    return this.http.get(url,[{ withCredentials: true }])
      .map(res => res.json());
  }

  add(body){
    console.log(body);
    let headerContent = new Headers();
    headerContent.append("Content-Type", "application/json");
    return this.http.post(this.baseUrl + 'api/user/portFee', body, { headers: headerContent })
            .map(res => res.json());
  }

  update(body){
    let headerContent = new Headers();
    headerContent.append("Content-Type", "application/json");
    return this.http.put(this.baseUrl + 'api/user/portFee', body, { headers: headerContent })
            .map(res => res.json());
  }
 
  deleteById(id) {
    return this.http.delete(this.baseUrl + 'api/user/portFee/' + id)
      .map(res => res.json());
  }

  
  
}