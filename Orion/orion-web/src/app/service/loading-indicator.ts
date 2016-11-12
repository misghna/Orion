import {Component} from '@angular/core';

export class LoadingPage {
    private loaderHidden: boolean;
    constructor() {
        this.loaderHidden = true;
    }

    hideLoader() {
        this.loaderHidden = true;
    }
    displayLoader() {
        this.loaderHidden = false;
    }
}

@Component({
    selector: 'loading-indicator',
    template: `
         <style type="text/css">
        html, body {
            height: 100%;
            margin: 0;
        }

        #loaderWrapper {
            min-width:100%;
            min-height: 100%; 
            background-color:#74767a;
            position: fixed;
            z-index: 999;
            opacity: 0.5;
        }
        #loader{
            display: block;
            margin-left:auto;
            margin-right:auto;
            vertical-align: middle;
            height: 100%;
            width:100%;
        }
    </style>
    <div id="loaderWrapper" class="hidden" [class.hidden]="loaderHidden">
        <img id="loader" src="http://localhost:8080/images/Loading.gif" alt="Loading..." >
    </div>
    `
})
export class LoadingIndicator {}