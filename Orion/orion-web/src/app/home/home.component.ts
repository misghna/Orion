import { Component, OnInit } from '@angular/core';
import { Http} from '@angular/http';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

    constructor(public http: Http) {}


  ngOnInit() {
  }

    logout() {
        console.log("method called");
        localStorage.setItem('access', "loggedout");
        this.http.get('http://localhost:8080/logout');
   }

}
