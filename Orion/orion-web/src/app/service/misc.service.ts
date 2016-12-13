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


  getAllItems(rev) {
    var url = this.baseUrl + 'api/user/items/' + rev;
    return this.http.get(url,[{ withCredentials: true }])
      .map(res => res.json());
  }

  getRevisions() {
    var url = this.baseUrl + 'api/user/item/revisions';
    return this.http.get(url,[{ withCredentials: true }])
      .map(res => res.json());
  }


  createNewRevision(rev){
        var body ={"rev":rev};
        var url = this.baseUrl + 'api/user/item/revisions';
        let headerContent = new Headers();      
        headerContent.append("Content-Type", "application/json");
        return this.http.post(url, body, { headers: headerContent })
                .map(res => res.json());
      }
    
    getItem(id) {
        let headerContent = new Headers();
        let options = new RequestOptions({ headers: headerContent });
        var url = this.baseUrl + 'api/user/item/' + id;
        return this.http.get(url,[{ headers: headerContent },{ withCredentials: true }])
          .map(res => res.json());
      }

  getItemNameList(){
        var url = this.baseUrl + 'api/user/item/names';
        return this.http.get(url,[{ withCredentials: true }])
          .map(res => res.json());
  }

  getItemNameBrandList(){
        var url = this.baseUrl + 'api/user/item/nameBrand';
        return this.http.get(url,[{ withCredentials: true }])
          .map(res => res.json());
  }

  getItemBrandList(){
        var url = this.baseUrl + 'api/user/item/brands';
        return this.http.get(url,[{ withCredentials: true }])
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
    return this.http.post(this.baseUrl + 'api/admin/item', body, { headers: headerContent })
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

}