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

    getItem(id) {
    let headerContent = new Headers();
    let options = new RequestOptions({ headers: headerContent });
    var url = this.baseUrl + 'api/user/item/' + id;
    return this.http.get(url,[{ headers: headerContent },{ withCredentials: true }])
      .map(res => res.json());
  }

    searchItem(searchTxt) {
    let headerContent = new Headers();
    var body = {"searchTxt":searchTxt};
    headerContent.append("Content-Type", "application/json");
    return this.http.post(this.baseUrl + '/api/user/item/search', body, { headers: headerContent })
            .map(res => res.json());
  }
  

  addItem(body){
    let headerContent = new Headers();
    headerContent.append("Content-Type", "application/json");
    return this.http.post(this.baseUrl + 'api/user/item', body, { headers: headerContent })
            .map(res => res.json());
  }

  updateItem(body){
    let headerContent = new Headers();
    headerContent.append("Content-Type", "application/json");
    return this.http.put(this.baseUrl + 'api/user/item', body, { headers: headerContent })
            .map(res => res.json());
  }

  deleteItem(itemId) {
    return this.http.delete(this.baseUrl + 'api/user/item/' + itemId)
      .map(res => res.json());
  }
}