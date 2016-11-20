import { Injectable ,isDevMode} from '@angular/core';
import { Router } from '@angular/router';

@Injectable()
export class UtilService {

constructor(public router: Router) {

  }

    redirectToLogin(){
      this.router.navigate(['/login']);
    }

    getBaseUrl(){
      if(isDevMode()){
        return 'http://localhost:8080/';     
      }else{
        return '';
      }
    }
} 