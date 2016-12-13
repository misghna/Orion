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
import { UserService } from './users/users.service';
import { MiscService } from './service/misc.service';
import {AppSettings} from './service/app.settings';
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
import { ItemsComponent } from './items/items.component';
import { FilterNamePipe } from './pipes/pipe.filterName';
import { SalesPlanComponent } from './sales-plan/sales-plan.component';
import { SalesPlanService } from './sales-plan/sales-plan.service';
import { OrdersComponent } from './orders/orders.component';
import { OrdersService } from './orders/orders.service';
import { FileUploadComponent } from './file-upload/file-upload.component';
import { BidService } from './bid/bid.service';
import { BidComponent } from './bid/bid.component';


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
                PassRenewComponent,
                ChangePasswordComponent,
                ItemsComponent,FilterNamePipe, SalesPlanComponent, OrdersComponent, FileUploadComponent,BidComponent
                 ],
  providers:[AuthGuard,UtilService,UserService,AppSettings,MiscService,SalesPlanService,OrdersService,BidService,
        {
          provide: Http,
          useFactory: (backend: XHRBackend, defaultOptions: RequestOptions, utilService:UtilService) => {
            return new HttpInterceptor(backend, defaultOptions,utilService);
            },
            deps: [ XHRBackend, RequestOptions,UtilService]
        },{
          provide : 'ApiEndpoint',  useValue : ('http://localhost:8080/')
        }
  ],

  bootstrap:    [ AppComponent]
})
export class AppModule { }