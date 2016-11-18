import { Injectable,Inject,isDevMode } from '@angular/core';
import { Http,Headers,RequestOptions } from '@angular/http';
import { UtilService } from './util.service';

import 'rxjs/add/operator/map';


@Injectable()
export class MiscService {
  baseUrl; 


  constructor(private http: Http,private util:UtilService) {
    this.baseUrl = util.getBaseUrl();
  }


  getAllItems() {
    let headerContent = new Headers();
    let options = new RequestOptions({ headers: headerContent });
    var url = this.baseUrl + 'api/user/items';
    return this.http.get(url,[{ headers: headerContent },{ withCredentials: true }])
      .map(res => res.json());
  }

}