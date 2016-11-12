import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import {RouterModule } from '@angular/router';
import { HttpModule, } from '@angular/http';
import { FormsModule } from '@angular/forms';
import { routes } from './app.routes';

import { AppComponent }  from './app.component';
import { LoginComponent }  from './login/login.component';
import { HomeComponent }  from './home/home.component';
import { AuthGuard } from './service/auth.guard';
import { UtilService } from './service/util.service';
import { UserService } from './service/user.service';
import {LoadingIndicator} from './service/loading-indicator';
import { RegisterComponent } from './register/register.component';
import { AdminComponent } from './admin/admin.component';
import { HeaderComponent } from './header/header.component';
import { UsersComponent } from './users/users.component';
import {DataTableModule} from "angular2-datatable";
import {Http, Request, RequestOptionsArgs, Response, XHRBackend, RequestOptions, ConnectionBackend, Headers} from '@angular/http';
import { HttpInterceptor } from "./service/httpInterceptor";
import { Router } from '@angular/router';
import { PassRenewComponent } from './passrenew/passrenew.component';
import { ChangePasswordComponent } from './change-password/change-password.component';

@NgModule({
  imports: [HttpModule, DataTableModule,
  BrowserModule, FormsModule,
  RouterModule.forRoot(routes, {
      useHash: false
    }) 
   ],
  declarations: [ AppComponent,
                LoginComponent,
                HomeComponent,
                RegisterComponent,
                AdminComponent,
                HeaderComponent,
                UsersComponent,
                LoadingIndicator,
                PassRenewComponent,
                ChangePasswordComponent
                 ],
  providers:[AuthGuard,UtilService,UserService,
        {
          provide: Http,
          useFactory: (backend: XHRBackend, defaultOptions: RequestOptions, utilService:UtilService) => {
            return new HttpInterceptor(backend, defaultOptions,utilService);
            },
            deps: [ XHRBackend, RequestOptions,UtilService]
        }
  ],


  bootstrap:    [ AppComponent]
})
export class AppModule { }