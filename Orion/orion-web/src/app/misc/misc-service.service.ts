import { Injectable,Inject,isDevMode } from '@angular/core';
import { Http,Headers,RequestOptions } from '@angular/http';
import Utils from '../service/utility';

import 'rxjs/add/operator/map';


@Injectable()
export class MiscSettingService {
  baseUrl; 


  constructor(private http: Http) {
    this.baseUrl = Utils.getBaseUrl();
  }


  get(){
    var url = this.baseUrl + 'api/user/miscSetting'
    return this.http.get(url,[{ withCredentials: true }])
      .map(res => res.json());
  }

  getPorts(){
    var url = this.baseUrl + 'api/user/miscSetting/ports'
    return this.http.get(url,[{ withCredentials: true }])
      .map(res => res.json());
  }

  getTerminals(){
    var url = this.baseUrl + 'api/user/miscSetting/terminals'
    return this.http.get(url,[{ withCredentials: true }])
      .map(res => res.json());
  }

  getImporters(){
    var url = this.baseUrl + 'api/user/miscSetting/importers'
    return this.http.get(url,[{ withCredentials: true }])
      .map(res => res.json());
  }
  
  getProp(name){
    var url = this.baseUrl + 'api/user/miscSetting/'+name
    return this.http.get(url,[{ withCredentials: true }])
      .map(res => res.json());
  }

  update(body){
    let headerContent = new Headers();
    headerContent.append("Content-Type", "application/json");
    return this.http.put(this.baseUrl + 'api/user/miscSetting', body, { headers: headerContent })
            .map(res => res.json());
  }
 
  
  
}