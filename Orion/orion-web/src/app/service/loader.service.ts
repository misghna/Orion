import {Component} from '@angular/core';
import {LoaderComponent, LoadingPage} from './loading-indicator';

@Component({
  selector: 'loading-demo',
  template: `
    <div [ngSwitch]="loading">
      <div *ngSwitchWhen="true">
        <loading-indicator></loading-indicator>
      </div>
      <div *ngSwitchWhen="false">
        <h2>BOOM!</h2>
        <hr/>
        <button (click)='again()'>Do it again!</button>
      </div>
    </div>
  `
})
export class Loader extends LoadingPage {
  constructor() {
    super(true);
    setTimeout(() => {
      this.ready();
    }, 5000);
  }
  
  again() {
    this.standby();
    setTimeout(() => {
      this.ready();
    }, 5000);
  }
}