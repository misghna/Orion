import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import {RouterModule } from '@angular/router';
import { HttpModule, } from '@angular/http';
import { FormsModule } from '@angular/forms';
import { routes } from './app.routes';
import { AppComponent }  from './app.component';
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
import { DateDiffPipe } from './pipes/pipe.dateDiff';
import { SalesPlanComponent } from './sales-plan/sales-plan.component';
import { SalesPlanService } from './sales-plan/sales-plan.service';
import { OrdersComponent } from './orders/orders.component';
import { OrdersService } from './orders/orders.service';
import { FileUploadComponent } from './file-upload/file-upload.component';
import { BidService } from './bid/bid.service';
import { ShippingService } from './shipping/shipping.service';
import { PaymentService } from './payment/payment.service';
import { BidComponent } from './bid/bid.component';
import { DocumentService } from './document/document.service';
import { HomeService } from './home/home.service';
import { PaymentComponent } from './payment/payment.component';
import { ShippingComponent } from './shipping/shipping.component';
import { FileSelectDirective, FileDropDirective, FileUploader } from 'ng2-file-upload/ng2-file-upload';
import {FileUploadModule} from 'ng2-file-upload/file-upload/file-upload.module'
import { DocumentComponent } from './document/document.component';
import { DigitsOnly } from './service/digitsOnly.directive';
import { NumberOnly } from './service/numberOnly.directive';
import { ContainerComponent } from './container/container.component';
import { ContainerService } from './container/container.service';
import { PortFeeComponent } from './port-fee/port-fee.component';
import { PortFeeService } from './port-fee/port-fee.service';
import { StatusComponent } from './status/status.component';
import { ApprovalComponent } from './approval/approval.component';
import { ApprovalService } from './approval/approval.service';
import { StatusService } from './status/status.service';
import { MiscComponent } from './misc/misc.component';
import { CurrencyComponent } from './currency/currency.component';
import { MiscSettingService } from './misc/misc-service.service';
import { CurrencyService } from './currency/currency.service';
import { LicenseComponent } from './license/license.component';
import { LicenseService } from './license/license.service';
import { AddressBookService } from './address-book/address-book.service';
import { BudgetComponent } from './budget/budget.component';
import { DocTrackingComponent } from './doc-tracking/doc-tracking.component';
import { DocTrackingService } from './doc-tracking/doc-tracking.service';
import { TerminalComponent } from './terminal/terminal.component';
import { TerminalService } from './terminal/terminal.service';
import { EstimateComponent } from './estimate/estimate.component';
import { EstimateService } from './estimate/estimate.service';
import { BudgetService } from './budget/budget.service';
import { DocHandoverComponent } from './doc-handover/doc-handover.component';
import { DocHandoverService } from './doc-handover/doc-handover.service';
import { NotificationComponent } from './notification/notification.component';
import { NotificationService } from './notification/notification.service';
import { AccessControlComponent } from './access-control/access-control.component';
import { CommonModule } from '@angular/common';
import { AgmCoreModule } from 'angular2-google-maps/core';
import { AddressBookComponent } from './address-book/address-book.component';
import { InvoiceFormatComponent } from './invoice-format/invoice-format.component';
import { InvoiceFormatService } from './invoice-format/invoice-format.service';
import { ExporterInvoiceComponent } from './exporter-invoice/exporter-invoice.component';
import { ChartsModule } from 'ng2-charts';
import { CashFlowComponent } from './cash-flow/cash-flow.component';

export function httpFactory(backend: XHRBackend, defaultOptions: RequestOptions, utilService:UtilService) {
            return new HttpInterceptor(backend, defaultOptions,utilService);}

@NgModule({
  imports: [HttpModule, DataTableModule,FileUploadModule,ChartsModule,
  BrowserModule, FormsModule,
  CommonModule,
  AgmCoreModule.forRoot({
      apiKey: 'AIzaSyC5Xn75hscrToxJ-ScGHTUevh58Pwoeyvw'
    }),
  RouterModule.forRoot(routes, {
      useHash: false
    }) 
   ],
  declarations: [ AppComponent,HomeComponent,RegisterComponent,AdminComponent,HeaderComponent,
                UsersComponent,PassRenewComponent,ChangePasswordComponent,
                ItemsComponent,FilterNamePipe, SalesPlanComponent, OrdersComponent, FileUploadComponent,
                BidComponent, PaymentComponent, ShippingComponent,LicenseComponent,
                DocumentComponent,DigitsOnly,NumberOnly, ContainerComponent,
                PortFeeComponent, StatusComponent, ApprovalComponent, MiscComponent, CurrencyComponent, 
                AddressBookComponent, BudgetComponent, DocTrackingComponent, TerminalComponent, EstimateComponent,DateDiffPipe,
                DocHandoverComponent, NotificationComponent, AccessControlComponent, AddressBookComponent, InvoiceFormatComponent, ExporterInvoiceComponent, CashFlowComponent
                 ],
  providers:[AuthGuard,UtilService,UserService,AppSettings,MiscService,
            SalesPlanService,OrdersService,DocumentService,
            BidService,PaymentService,ShippingService,HomeService,
            ContainerService,PortFeeService,ApprovalService,StatusService,
            MiscSettingService,CurrencyService,AddressBookService,LicenseService,
            DocTrackingService,TerminalService,EstimateService,BudgetService,
            DocHandoverService,NotificationService,InvoiceFormatService,
        {
          provide: Http,
          useFactory: httpFactory,
            deps: [ XHRBackend, RequestOptions,UtilService]
        },
        {
          provide : 'ApiEndpoint',  useValue : ('http://localhost:8080/')
        }
  ],

  bootstrap:    [ AppComponent]
})

export class AppModule { }