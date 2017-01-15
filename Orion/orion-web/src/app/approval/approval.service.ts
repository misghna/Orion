import { Injectable,Inject,isDevMode } from '@angular/core';
import { Http,Headers,RequestOptions } from '@angular/http';
import Utils from '../service/utility';

import 'rxjs/add/operator/map';


@Injectable()
export class ApprovalService {
  baseUrl; 


  constructor(private http: Http) {
    this.baseUrl = Utils.getBaseUrl();
  }


  get(id) {
    var url = this.baseUrl + 'api/user/approval/' + id;
    return this.http.get(url,[{ withCredentials: true }])
      .map(res => res.json());
  }

  // getReportPreview(id,type) {
  //   let headerContent = new Headers();
  //   headerContent.append("Content-Type", "application/html");
  //   var url = this.baseUrl + 'api/user/report/generateOrderAuthPreview/' + id;
  //   return this.http.get(url,[{ withCredentials: true },{ headers: headerContent }]);
  // }

  getReportPreview(body){
    let headerContent = new Headers();
    headerContent.append("Content-Type", "application/json");
    return this.http.post(this.baseUrl + 'api/user/report/preview', body, { headers: headerContent });
  }

  add(body){
    let headerContent = new Headers();
    headerContent.append("Content-Type", "application/json");
    return this.http.post(this.baseUrl + 'api/user/approval', body, { headers: headerContent })
            .map(res => res.json());
  }
  
  approve(body){
    let headerContent = new Headers();
    headerContent.append("Content-Type", "application/json");
    return this.http.put(this.baseUrl + 'api/user/approve', body, { headers: headerContent })
            .map(res => res.json());
  }

  voidApproval(id){
    let headerContent = new Headers();
    headerContent.append("Content-Type", "application/json");
    return this.http.put(this.baseUrl + 'api/user/approval/void/' + id, null, { headers: headerContent })
            .map(res => res.json());
  }
 
  // deleteById(id) {
  //   return this.http.delete(this.baseUrl + 'api/user/approve/' + id)
  //     .map(res => res.json());
  // }

  
  getPaymentList(){
      return ['Frwd. Agent Fee','Bromangol','Customs','Port','Seller','Terminal','Transport'];
  }

  
}