import { Injectable,Inject,isDevMode } from '@angular/core';
import { Http,Headers,RequestOptions } from '@angular/http';
import { UtilService } from '../service/util.service';

import 'rxjs/add/operator/map';


@Injectable()
export class UserService {
  baseUrl; // = AppSettings.BASE_URL;

//@Inject('ApiEndpoint') private apiEndpoint: string

  constructor(private http: Http,private util:UtilService) {
    this.baseUrl = util.getBaseUrl();
  }

signup(body){
    let headerContent = new Headers();
    headerContent.append("Content-Type", "application/json");
    return this.http.post(this.baseUrl + 'api/open/user', body, { headers: headerContent })
     .map(res => res.json());
}

getAllUsers() {
    let headerContent = new Headers();
    let options = new RequestOptions({ headers: headerContent });
    var url = this.baseUrl + 'api/admin/users';
    return this.http.get(url,[{ headers: headerContent },{ withCredentials: true }])
      .map(res => res.json());
  }

getUser() {
    let headerContent = new Headers();
    let options = new RequestOptions({ headers: headerContent });
    var url = this.baseUrl + 'api/user/user';
    return this.http.get(url,[{ headers: headerContent },{ withCredentials: true }])
      .map(res => res.json());
  }

  getApprovers(type) {
    var url = this.baseUrl + 'api/user/approvers/' + type;
    return this.http.get(url,[{ withCredentials: true }])
      .map(res => res.json());
  }

  check() {
    let headerContent = new Headers();
    let options = new RequestOptions({ headers: headerContent });
    var url = this.baseUrl + 'api/check';
    return this.http.get(url,[{ headers: headerContent },{ withCredentials: true }])
      .map(res => res.json());
  }
  

  changeStatus(userId,status) {
    var body = {'userId':userId,"status":status};
    let headerContent = new Headers();
    headerContent.append("Content-Type", "application/json");
    return this.http.put(this.baseUrl + 'api/admin/changeStatus', body, { headers: headerContent })
      .map(res => res.json());
  }


  updateNotifications(body) {
    let headerContent = new Headers();
    headerContent.append("Content-Type", "application/json");
    return this.http.put(this.baseUrl + 'api/user/notification', body, { headers: headerContent })
      .map(res => res.json());
  }

  reqVCode(emailId) {
    var body = {'email':emailId};
    let headerContent = new Headers();
    headerContent.append("Content-Type", "application/json");
    return this.http.post(this.baseUrl + 'api/open/reqCode', body, { headers: headerContent })
      .map(res => res.json());
  }

  changeForgotenPass(body){
    let headerContent = new Headers();
    headerContent.append("Content-Type", "application/json");
    return this.http.post(this.baseUrl + 'api/open/changeForgotenPass', body, { headers: headerContent })
      .map(res => res.json());
  }

  update(body){
    let headerContent = new Headers();
    headerContent.append("Content-Type", "application/json");
    return this.http.put(this.baseUrl + 'api/admin/user', body, { headers: headerContent })
      .map(res => res.json());
  }

  changePass(body){
    let headerContent = new Headers();
    headerContent.append("Content-Type", "application/json");
    return this.http.post(this.baseUrl + 'api/user/changePass', body, { headers: headerContent })
      .map(res => res.json());
  }

  // login(email,password){
  //   let headerContent = new Headers();
  //   headerContent.append("Authorization", "Basic " + btoa(email + ":" + password)); 
  //   headerContent.append("Content-Type", "application/x-www-form-urlencoded");
  //   return this.http.post(this.baseUrl + 'api/user/login', null, { headers: headerContent })
  //        .map(res => res.json());
  // }

  changeRole(userId,role) {
    var body = {'userId':userId,"role":role};
    let headerContent = new Headers();
    headerContent.append("Content-Type", "application/json");
    return this.http.put(this.baseUrl + 'api/admin/changeRole', body, { headers: headerContent })
      .map(res => res.json());
  }

  deleteUser(userId) {
    return this.http.delete(this.baseUrl + 'api/admin/deleteUser/' + userId)
      .map(res => res.json());
  }


  logoutUsers() {
    this.util.redirectToLogin();
    this.http.get(this.baseUrl + 'logout')
      .subscribe(
        response => {
          console.log("signed out");
        },
        error => {
          console.error(error);
        }
      );
  }
}