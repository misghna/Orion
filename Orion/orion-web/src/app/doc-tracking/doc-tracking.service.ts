import { Injectable,Inject,isDevMode } from '@angular/core';
import { Http,Headers,RequestOptions } from '@angular/http';
import Utils from '../service/utility';

import 'rxjs/add/operator/map';


@Injectable()
export class DocTrackingService {
  baseUrl; 


  constructor(private http: Http) {
    this.baseUrl = Utils.getBaseUrl();
  }


  get(id) {
    var url = this.baseUrl + 'api/user/docTrack/' + id;
    return this.http.get(url,[{ withCredentials: true }])
      .map(res => res.json());
  }

  track(id) {
    var url = this.baseUrl + 'api/user/docTrack/track/' + id;
    return this.http.get(url,[{ withCredentials: true }]);
  }
  

  add(body,state){
    let headerContent = new Headers();
    headerContent.append("Content-Type", "application/json");
    return this.http.post(this.baseUrl + 'api/user/docTrack/' + state, body, { headers: headerContent })
            .map(res => res.json());
  }

  update(body,state){
    let headerContent = new Headers();
    headerContent.append("Content-Type", "application/json");
    return this.http.put(this.baseUrl + 'api/user/docTrack/' + state, body, { headers: headerContent })
            .map(res => res.json());
  }
 
  deleteById(state,id) {
    return this.http.delete(this.baseUrl + 'api/user/docTrack/' + state + '/' +  id)
      .map(res => res.json());
  }

  
  
}