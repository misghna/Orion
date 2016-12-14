// Import our dependencies
import { Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { LoginComponent } from './login/login.component';
import { UsersComponent } from './users/users.component';
import { RegisterComponent } from './register/register.component';
import { PassRenewComponent } from './passrenew/passrenew.component';
import { ItemsComponent } from './items/items.component';
import { SalesPlanComponent } from './sales-plan/sales-plan.component';
import { OrdersComponent } from './orders/orders.component';
import { BidComponent } from './bid/bid.component';
import { PaymentComponent } from './payment/payment.component';
import { ShippingComponent } from './shipping/shipping.component';
import { FileUploadComponent } from './file-upload/file-upload.component';

import { AuthGuard } from './service/auth.guard';
import { ChangePasswordComponent } from './change-password/change-password.component';


// Define which component should be loaded based on the current URL
export const routes: Routes = [
  { path: '',   component: HomeComponent, canActivate: [AuthGuard] },
  { path: 'login',  component: LoginComponent },
  { path: 'logout',  component: LoginComponent },
  { path: 'setting/users',  component: UsersComponent },
  { path: 'setting/salesPlan',  component: SalesPlanComponent },
  { path: 'import/orders',  component: OrdersComponent },
  { path: 'import/payment/:id',  component: PaymentComponent },
  { path: 'import/shipping/:id',  component: ShippingComponent },
  { path: 'import/bid/:id',  component: BidComponent },
  { path: 'setting/items',  component: ItemsComponent },
   { path: 'fileUpload',  component: FileUploadComponent },
  { path: 'other/register',  component: RegisterComponent },
  { path: 'other/changePassowrd',  component: ChangePasswordComponent },
  { path: 'other/passrenew',  component: PassRenewComponent },
  { path: '**',     component: LoginComponent },
];