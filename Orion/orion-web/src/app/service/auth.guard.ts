import { Injectable } from '@angular/core';
import { Router, CanActivate } from '@angular/router';

@Injectable()
export class AuthGuard implements CanActivate {
  constructor(private router: Router) {}

  canActivate() {
    // Check to see if a user has a valid JWT
    var userDetails = JSON.parse(localStorage.getItem('accessDetail'));
    if(userDetails!=null && userDetails['access'] =='granted'){
      return true;
    }

    // If not, they redirect them to the login page
    this.router.navigate(['/login']);
    return false;
  }

}