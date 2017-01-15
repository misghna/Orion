import { Injectable,Inject,isDevMode } from '@angular/core';
import { Http,Headers,RequestOptions } from '@angular/http';
import Utils from '../service/utility';

import 'rxjs/add/operator/map';


@Injectable()
export class LicenseService {
  baseUrl; 


  constructor(private http: Http) {
    this.baseUrl = Utils.getBaseUrl();
  }


  get(id) {
    var url = this.baseUrl + 'api/user/lic/' + id;
    return this.http.get(url,[{ withCredentials: true }])
      .map(res => res.json());
  }

  add(body,state){
    let headerContent = new Headers();
    headerContent.append("Content-Type", "application/json");
    return this.http.post(this.baseUrl + 'api/user/lic/' + state, body, { headers: headerContent })
            .map(res => res.json());
  }

  update(body,state){
    let headerContent = new Headers();
    headerContent.append("Content-Type", "application/json");
    return this.http.put(this.baseUrl + 'api/user/lic/' + state, body, { headers: headerContent })
            .map(res => res.json());
  }
 
  deleteById(state,id) {
    return this.http.delete(this.baseUrl + 'api/user/lic/' + state + '/' +  id)
      .map(res => res.json());
  }

  
  
}