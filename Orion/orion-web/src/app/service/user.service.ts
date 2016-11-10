import { Injectable } from '@angular/core';
import { Http,Headers,RequestOptions } from '@angular/http';
import { UtilService } from './util.service';
import 'rxjs/add/operator/map';


@Injectable()
export class UserService {

  constructor(private http: Http,private util:UtilService) {}

getAllUsers() {
    let headerContent = new Headers();
    let options = new RequestOptions({ headers: headerContent });
    var url = 'http://localhost:8080/api/users';
    return this.http.get(url,[{ headers: headerContent },{ withCredentials: true }])
      .map(res => res.json());
  }

  changeStatus(userId,status) {
    var body = {'userId':userId,"status":status};
    let headerContent = new Headers();
    headerContent.append("Content-Type", "application/json");
    return this.http.put('http://localhost:8080/api/admin/changeStatus', body, { headers: headerContent })
      .map(res => res.json());
  }

  changeRole(userId,role) {
    var body = {'userId':userId,"role":role};
    let headerContent = new Headers();
    headerContent.append("Content-Type", "application/json");
    return this.http.put('http://localhost:8080/api/admin/changeRole', body, { headers: headerContent })
      .map(res => res.json());
  }

  deleteUser(userId) {
    return this.http.delete('http://localhost:8080/api/admin/deleteUser/' + userId)
      .map(res => res.json());
  }


  logoutUsers() {
    this.util.redirectToLogin();
    this.http.get('http://localhost:8080/logout')
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