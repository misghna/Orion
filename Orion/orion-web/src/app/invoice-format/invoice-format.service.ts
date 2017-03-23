import { Injectable,Inject,isDevMode } from '@angular/core';
import { Http,Headers,RequestOptions } from '@angular/http';
import Utils from '../service/utility';

import 'rxjs/add/operator/map';


@Injectable()
export class InvoiceFormatService {
  baseUrl; 


  constructor(private http: Http) {
    this.baseUrl = Utils.getBaseUrl();
  }


  get(){
    var url = this.baseUrl + 'api/user/invoiceFormat'
    return this.http.get(url,[{ withCredentials: true }])
      .map(res => res.json());
  }

  getInvoiceFormat(id){
    var url = this.baseUrl + 'api/user/invoiceFormat/format/' + id
    return this.http.get(url,[{ withCredentials: true }]);
  }

  getInvoice(param){
     let headerContent = new Headers();
    headerContent.append("Content-Type", "application/json");
    return this.http.post(this.baseUrl + 'api/user/invoice/get', param, { headers: headerContent });
  }


  createInvoice(param){
        let headerContent = new Headers();
        headerContent.append("Content-Type", "application/json");
    return this.http.post(this.baseUrl + 'api/user/invoice', param, { headers: headerContent });
  }

  add(body){
    let headerContent = new Headers();
    headerContent.append("Content-Type", "application/json");
    return this.http.post(this.baseUrl + 'api/user/invoiceFormat', body, { headers: headerContent })
            .map(res => res.json());
  }

  update(body){
    let headerContent = new Headers();
    headerContent.append("Content-Type", "application/json");
    return this.http.put(this.baseUrl + 'api/user/invoiceFormat', body, { headers: headerContent })
            .map(res => res.json());
  }

  updateInvoice(body){
    let headerContent = new Headers();
    headerContent.append("Content-Type", "application/json");
    return this.http.put(this.baseUrl + 'api/user/invoice', body, { headers: headerContent });
  }
 
  deleteById(id) {
    return this.http.delete(this.baseUrl + 'api/user/invoiceFormat/' + id)
      .map(res => res.json());
  }

  
  
}