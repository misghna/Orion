import { Injectable,Inject,isDevMode } from '@angular/core';
import { Http,Headers,RequestOptions } from '@angular/http';
import Utils from '../service/utility';

import 'rxjs/add/operator/map';


@Injectable()
export class BudgetService {
  baseUrl; 


  constructor(private http: Http) {
    this.baseUrl = Utils.getBaseUrl();  

  }


  getSalesPlan(year,month) {
    var url = this.baseUrl + 'api/user/budget/' + year + '/' + month;
    return this.http.get(url,[{ withCredentials: true }])
      .map(res => res.json());
  }

   duplicatePlan(body){
        var url = this.baseUrl + 'api/admin/salesPlan/duplicatePlan';
        let headerContent = new Headers();      
        headerContent.append("Content-Type", "application/json");
        return this.http.post(url, body, { headers: headerContent })
                .map(res => res.json());
    }

  addSalesPlan(body,year,month){
    let headerContent = new Headers();
    headerContent.append("Content-Type", "application/json");
    return this.http.post(this.baseUrl + 'api/admin/salesPlan/' + year + '/' + month, body, { headers: headerContent })
            .map(res => res.json());
  }

  updateSalesPlan(body,year,month){
    let headerContent = new Headers();
    headerContent.append("Content-Type", "application/json");
    return this.http.put(this.baseUrl + 'api/admin/salesPlan/' + year + '/' + month, body, { headers: headerContent })
            .map(res => res.json());
  }

  deleteSalesPlanById(planId,year,month) {
    return this.http.delete(this.baseUrl + 'api/admin/salesPlan/' + planId + '/' + year + '/' + month)
      .map(res => res.json());
  }

  deleteSalesPlan(year,month) {
    return this.http.delete(this.baseUrl + 'api/admin/salesPlan/' + year + '/' + month)
      .map(res => res.json());
  }
  
}