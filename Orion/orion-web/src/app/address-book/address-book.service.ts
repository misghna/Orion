import { Injectable,Inject,isDevMode } from '@angular/core';
import { Http,Headers,RequestOptions } from '@angular/http';
import Utils from '../service/utility';

import 'rxjs/add/operator/map';


@Injectable()
export class AddressBookService {
  baseUrl; 


  constructor(private http: Http) {
    this.baseUrl = Utils.getBaseUrl();
  }


  get(type){
    var url = this.baseUrl + 'api/user/addressBook/' + type
    return this.http.get(url,[{ withCredentials: true }])
      .map(res => res.json());
  }

  // getByType(type){
  //   var url = this.baseUrl + 'api/user/addressBook/all'
  //   return this.http.get(url,[{ withCredentials: true }])
  //     .map(res => res.json());
  // }

  getDestinations(){
    var url = this.baseUrl + 'api/user/addressBook/destinations'
    return this.http.get(url,[{ withCredentials: true }])
      .map(res => res.json());
  }


  getFWAgents(){
    var url = this.baseUrl + 'api/user/addressBook/fwAgents'
    return this.http.get(url,[{ withCredentials: true }])
      .map(res => res.json());
  }

  add(body){
    console.log(body);
    let headerContent = new Headers();
    headerContent.append("Content-Type", "application/json");
    return this.http.post(this.baseUrl + 'api/user/addressBook', body, { headers: headerContent })
            .map(res => res.json());
  }

  update(body){
    let headerContent = new Headers();
    headerContent.append("Content-Type", "application/json");
    return this.http.put(this.baseUrl + 'api/user/addressBook', body, { headers: headerContent })
            .map(res => res.json());
  }
 
  deleteById(id) {
    return this.http.delete(this.baseUrl + 'api/user/addressBook/' + id)
      .map(res => res.json());
  }

  
  
}