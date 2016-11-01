// Import our dependencies
import { Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { LoginComponent } from './login/login.component';
import { LogoutComponent } from './login/logout.component';
import { AuthGuard } from './common/auth.guard';

// Define which component should be loaded based on the current URL
export const routes: Routes = [
  { path: '',   component: HomeComponent, canActivate: [AuthGuard] },
  { path: 'login',  component: LoginComponent },
  { path: 'logout',  component: LoginComponent },
  { path: '**',     component: LoginComponent },
];