// Import our dependencies
import { Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { UsersComponent } from './users/users.component';
import { RegisterComponent } from './register/register.component';
import { PassRenewComponent } from './passrenew/passrenew.component';
import { ItemsComponent } from './items/items.component';
import { SalesPlanComponent } from './sales-plan/sales-plan.component';
import { OrdersComponent } from './orders/orders.component';
import { BidComponent } from './bid/bid.component';
import { PaymentComponent } from './payment/payment.component';
import { ShippingComponent } from './shipping/shipping.component';
import { ContainerComponent } from './container/container.component';
import { FileUploadComponent } from './file-upload/file-upload.component';
import { DocumentComponent } from './document/document.component';
import { PortFeeComponent } from './port-fee/port-fee.component';
import { StatusComponent } from './status/status.component';
import { ApprovalComponent } from './approval/approval.component';
import { MiscComponent } from './misc/misc.component';
import { CurrencyComponent } from './currency/currency.component';


import { AuthGuard } from './service/auth.guard';
import { ChangePasswordComponent } from './change-password/change-password.component';


// Define which component should be loaded based on the current URL
export const routes: Routes = [
  { path: '',   component: HomeComponent},
  // { path: '',   component: HomeComponent, canActivate: [AuthGuard] },
  // { path: 'login',  component: LoginComponent },
  // { path: 'logout',  component: LoginComponent },
  { path: 'setting/items',  component: ItemsComponent },
  { path: 'setting/users',  component: UsersComponent },
  { path: 'setting/salesPlan',  component: SalesPlanComponent },
  { path: 'setting/legalization',  component: PortFeeComponent },
  { path: 'setting/misc',  component: MiscComponent },
  { path: 'setting/currency',  component: CurrencyComponent },
  { path: 'status',  component: StatusComponent },
  { path: 'finance/approval/:id',  component: ApprovalComponent },
  { path: 'import/order/:id',  component: OrdersComponent },
  { path: 'finance/payment/:id',  component: PaymentComponent },
  { path: 'import/shipping/:id',  component: ShippingComponent },
  { path: 'import/container/:id',  component: ContainerComponent },
  { path: 'import/bid/:id',  component: BidComponent },
  
  // { path: 'fileUpload',  component: FileUploadComponent },
  { path: 'document/:id',  component: DocumentComponent },

  { path: 'open/register',  component: RegisterComponent },
  { path: 'open/passrenew',  component: PassRenewComponent },


  { path: 'other/changePassowrd',  component: ChangePasswordComponent },

  { path: '**',     component: HomeComponent},
];