import { Injectable } from '@angular/core';
import { Router } from '@angular/router';

@Injectable()
export class UtilService {

constructor(public router: Router) {

  }

    redirectToLogin(){
      this.router.navigate(['/login']);
    }
 // isNavBarHidden:boolean;
}