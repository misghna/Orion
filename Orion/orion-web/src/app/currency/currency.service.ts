import { Injectable,Inject,isDevMode } from '@angular/core';
import { Http,Headers,RequestOptions } from '@angular/http';
import Utils from '../service/utility';

import 'rxjs/add/operator/map';


@Injectable()
export class CurrencyService {
  baseUrl; 


  constructor(private http: Http) {
    this.baseUrl = Utils.getBaseUrl();
  }


  get(){
    var url = this.baseUrl + 'api/user/exchange'
    return this.http.get(url,[{ withCredentials: true }])
      .map(res => res.json());
  }

  getCurrency(){
    var url = this.baseUrl + 'api/user/currency'
    return this.http.get(url,[{ withCredentials: true }])
      .map(res => res.json());
  }

  add(body){
    console.log(body);
    let headerContent = new Headers();
    headerContent.append("Content-Type", "application/json");
    return this.http.post(this.baseUrl + 'api/user/exchange', body, { headers: headerContent })
            .map(res => res.json());
  }

  update(body){
    let headerContent = new Headers();
    headerContent.append("Content-Type", "application/json");
    return this.http.put(this.baseUrl + 'api/user/exchange', body, { headers: headerContent })
            .map(res => res.json());
  }
 
  deleteById(id) {
    return this.http.delete(this.baseUrl + 'api/user/exchange/' + id)
      .map(res => res.json());
  }



filterCurrency(txt,data){
  if(txt.length<1){
      return data
   }
  return data.filter(el => (
            (el.country!=null && el.country.toLowerCase().indexOf(txt.toLowerCase()) !== -1) || 
             (el.currency!=null && el.currency.toLowerCase().indexOf(txt.toLowerCase()) !== -1) ||
             (el.abrevation!=null && el.abrevation.toLowerCase().indexOf(txt.toLowerCase()) !== -1)));
}
  
  
}