import { Component,Input,OnInit,Output,ElementRef,ViewChild,AfterViewInit,Renderer,EventEmitter} from '@angular/core';

import { Http,Headers } from '@angular/http';
import { UserService } from '../users/users.service';
import { UtilService } from '../service/util.service';
import {Observable} from 'rxjs/Observable';

declare var $: any;

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {
  navHidden;
  @Input() optionsObj = "";

  @Output() activateSearch = new EventEmitter();
  @Output() activateOptions = new EventEmitter();

   @ViewChild('myModalBtn') modalInput:ElementRef;
   @Output() deleteEmtter = new EventEmitter();

  regUser = true;
  modalEl;modalMsg;subscription;
  delTask;title;

  constructor(private http:Http, private userService:UserService, 
              private utilService:UtilService,private rd: Renderer,private el: ElementRef) {
    this.navHidden = false;
    this.subscription = utilService.currentHeaderState$.subscribe(
      state => {   
        this.navHidden = !state;
    });

    this.subscription = utilService.currentModalState$.subscribe(
      delInfo => {   
        this.title = delInfo['title'];
        this.modalMsg = delInfo['msg'];
        this.delTask = delInfo['task'];
        let event = new MouseEvent('click', {bubbles: true});
        this.rd.invokeElementMethod(
          this.modalInput.nativeElement, 'dispatchEvent', [event]);
    });


       $(this.el.nativeElement).on('click','.dropdown-menu li',function(){
          console.log($(this).index());
          $(this).parent().parent().parent().find(".active").removeClass("active");
          $(this).parent().parent().addClass("active");      
        });
   }

  ngOnInit() {
      var access = JSON.parse(localStorage.getItem('accessDetail'));
      if(access!=null && access['role'] =='Admin'){
        this.regUser = false;
      }
  }


  logout(){
       localStorage.setItem('accessDetail', "{}");
        this.userService.logoutUsers();
   }

   search(searchTxt){
     this.activateSearch.emit({"searchTxt":searchTxt});
   }

   triggerOption(optionName){
     this.activateOptions.emit({"optionName":optionName});
   }

   deleteItem(){
     this.deleteEmtter.emit({"delTask":this.delTask});
   }



}
