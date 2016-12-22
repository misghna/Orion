import {Directive,HostListener} from '@angular/core';

@Directive({
  selector: '[digitsonly]'
})
export class DigitsOnly {

  @HostListener('keypress') onkeypress(e){
        let event = e || window.event;
        if(event){
          return this.isNumberKey(event);
        }
      }

  isNumberKey(event){
     let charCode = (event.which) ? event.which : event.keyCode;
     if (charCode!=46 && charCode > 31 && (charCode < 48 || charCode > 57)){
        return false;
     }
     return true;
  }


  constructor() {
  }



}