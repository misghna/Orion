import { Component, OnInit,ElementRef } from '@angular/core';
import { Http} from '@angular/http';
import { UtilService } from '../service/util.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})



export class HomeComponent implements OnInit {

  constructor(private utilService:UtilService, private http:Http, private elementRef: ElementRef) {  

  }
      
  ngOnInit() {
  }



}
