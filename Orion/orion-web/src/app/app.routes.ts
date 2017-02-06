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
import { LicenseComponent } from './license/license.component';
import { ClientComponent } from './client/client.component';
import { DocTrackingComponent } from './doc-tracking/doc-tracking.component';
import { TerminalComponent } from './terminal/terminal.component';
import { EstimateComponent } from './estimate/estimate.component';
import { DocHandoverComponent } from './doc-handover/doc-handover.component';
import { NotificationComponent } from './notification/notification.component';

import { BudgetComponent } from './budget/budget.component';

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
  { path: 'setting/addressBook',  component: ClientComponent },
  { path: 'setting/terminal',  component: TerminalComponent },
  

  { path: 'status/:id',  component: StatusComponent },
  { path: 'finance/approval/:id',  component: ApprovalComponent },
  { path: 'finance/payment/:id',  component: PaymentComponent },
  { path: 'finance/estimator/:id',  component: EstimateComponent },
  { path: 'finance/budget/:id',  component: BudgetComponent },
  
  
  { path: 'import/order/:id',  component: OrdersComponent },
  { path: 'import/shipping/:id',  component: ShippingComponent },
  { path: 'import/container/:id',  component: ContainerComponent },
  { path: 'import/bid/:id',  component: BidComponent },
  
  { path: 'document/license/:id',  component: LicenseComponent },
  { path: 'document/docTracking/:id',  component: DocTrackingComponent },
  { path: 'document/uploaded/:id',  component: DocumentComponent },
  { path: 'document/handover/:id',  component: DocHandoverComponent },
  

 { path: 'notification',  component: NotificationComponent },
  

  { path: 'open/register',  component: RegisterComponent },
  { path: 'open/passrenew',  component: PassRenewComponent },


  { path: 'other/changePassowrd',  component: ChangePasswordComponent },

  { path: '**',     component: HomeComponent},
];