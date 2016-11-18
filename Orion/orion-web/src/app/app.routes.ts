// Import our dependencies
import { Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { LoginComponent } from './login/login.component';
import { UsersComponent } from './users/users.component';
import { RegisterComponent } from './register/register.component';
import { PassRenewComponent } from './passrenew/passrenew.component';
import { ItemsComponent } from './items/items.component';
import { AuthGuard } from './service/auth.guard';
import { ChangePasswordComponent } from './change-password/change-password.component';


// Define which component should be loaded based on the current URL
export const routes: Routes = [
  { path: '',   component: HomeComponent, canActivate: [AuthGuard] },
  { path: 'login',  component: LoginComponent },
  { path: 'logout',  component: LoginComponent },
  { path: 'admin/users',  component: UsersComponent },
  { path: 'items',  component: ItemsComponent },
  { path: 'register',  component: RegisterComponent },
  { path: 'changePassowrd',  component: ChangePasswordComponent },
  { path: 'passrenew',  component: PassRenewComponent },
  { path: '**',     component: LoginComponent },
];