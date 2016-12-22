import {Http, Request, RequestOptionsArgs, Response, XHRBackend, RequestOptions,
         ConnectionBackend, Headers} from '@angular/http';
import { UtilService } from './util.service';
import { Router } from '@angular/router';

import { Observable } from "rxjs/Observable";
import "rxjs/add/operator/catch";
import "rxjs/add/observable/throw";
import "rxjs/add/observable/empty";
import 'rxjs/add/operator/do';

export class HttpInterceptor extends Http {

    sent = 0;total =0;state =false;refererReset=false;
    constructor(connectionBackend: ConnectionBackend, defaultOptions: RequestOptions,public util:UtilService) {
        super(connectionBackend, defaultOptions);

    }
    
    request(url: string | Request, options?: RequestOptionsArgs): Observable<Response> {        
        this.util.setLoaderState(true);
        this.state=true;
        this.sent = this.sent + 1 ;

        return this.intercept(super.request(url, options));
    }

    get(url: string, options?: RequestOptionsArgs): Observable<Response> {
        return this.intercept(super.get(url, options));
    }


    post(url: string, body: string, options?: RequestOptionsArgs): Observable<Response> {
        console.log("overriding ...");
        return this.intercept(super.post(url, body, options));
    }

    put(url: string, body: string, options?: RequestOptionsArgs): Observable<Response> {
        return this.intercept(super.put(url, body, options));
    }

    delete(url: string, options?: RequestOptionsArgs): Observable<Response> {
        return this.intercept(super.delete(url, options));
    }

    intercept(observable: Observable<Response>): Observable<Response> {

        return observable.catch(this.onCatch)
            .do((res: Response) => {
                this.onSubscribeSuccess(res);
            },(error: any) => {
                this.onSubscribeError(error);
            });

    }

    private onCatch(error: any, caught: Observable<any>): Observable<any> {
            this.total = this.total + 1;
            if(this.total >= this.sent * 2){
                this.util.setLoaderState(false);
            }

       //      console.log("catch()  " + error);

            if (error.status  === 401) {
                console.error("401 error");
                window.location.replace("/login");
                return Observable.empty();
            } else if (error.status ==0 || error.status==302){
                console.error("302 redirecting..");

                 return Observable.empty();
            }else {
                return Observable.throw(error);
            }
    //    return Observable.throw(error);
    }

    private onSubscribeSuccess(res: Response): void {

        if(res.url.indexOf("login")>=0 ){
            this.util.redirectToLogin();
        }
       this.total = this.total + 1;
        if(this.total >= this.sent * 2){
            this.util.setLoaderState(false);
        }
    }

    private onSubscribeError(error: any): void {
   //     console.log("error()  " + error);
        this.total = this.total + 1;
        if(this.total >= this.sent * 2){
            this.util.setLoaderState(false);
        }
     //   console.error(error);
    }


}