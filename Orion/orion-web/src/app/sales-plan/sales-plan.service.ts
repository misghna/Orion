import { Injectable,Inject,isDevMode } from '@angular/core';
import { Http,Headers,RequestOptions } from '@angular/http';
import Utils from '../service/utility';

import 'rxjs/add/operator/map';


@Injectable()
export class SalesPlanService {
  baseUrl; 


  constructor(private http: Http) {
    this.baseUrl = Utils.getBaseUrl();  

  }


  getSalesPlan(year,month) {
    var url = this.baseUrl + 'api/user/salesPlan/' + year + '/' + month;
    return this.http.get(url,[{ withCredentials: true }])
      .map(res => res.json());
  }

   duplicatePlan(body){
        var url = this.baseUrl + 'api/user/salesPlan/duplicatePlan';
        let headerContent = new Headers();      
        headerContent.append("Content-Type", "application/json");
        return this.http.post(url, body, { headers: headerContent })
                .map(res => res.json());
    }

  addSalesPlan(body,year,month){
    let headerContent = new Headers();
    headerContent.append("Content-Type", "application/json");
    return this.http.post(this.baseUrl + 'api/user/salesPlan/' + year + '/' + month, body, { headers: headerContent })
            .map(res => res.json());
  }

  updateSalesPlan(body,year,month){
    let headerContent = new Headers();
    headerContent.append("Content-Type", "application/json");
    return this.http.put(this.baseUrl + 'api/user/salesPlan/' + year + '/' + month, body, { headers: headerContent })
            .map(res => res.json());
  }

  deleteSalesPlanById(planId,year,month) {
    return this.http.delete(this.baseUrl + 'api/user/salesPlan/' + planId + '/' + year + '/' + month)
      .map(res => res.json());
  }

  deleteSalesPlan(year,month) {
    return this.http.delete(this.baseUrl + 'api/user/salesPlan/' + year + '/' + month)
      .map(res => res.json());
  }
  
}