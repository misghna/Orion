import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import {RouterModule } from '@angular/router';
import { HttpModule } from '@angular/http';
import { FormsModule } from '@angular/forms';
import { routes } from './app.routes';

import { AppComponent }  from './app.component';
import { LoginComponent }  from './login/login.component';
import { LogoutComponent }  from './login/logout.component';
import { HomeComponent }  from './home/home.component';
import { AuthGuard } from './common/auth.guard';

@NgModule({
  imports: [HttpModule, BrowserModule, FormsModule,
  RouterModule.forRoot(routes, {
      useHash: false
    }) 
   ],
  declarations: [ AppComponent,
                LoginComponent,
                HomeComponent,
                LogoutComponent ],
  providers:[AuthGuard],
  bootstrap:    [ AppComponent ]
})
export class AppModule { }