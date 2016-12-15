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

    sent = 0;total =0;state =false;
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
           // this.total = this.total + 1 ;           

    //    var myObservable =  observable.subscribe(r => {
                // this.total = this.total + 1;
                // if(this.total >= this.sent * 2){
                //  this.util.setLoaderState(false);
                // }
    //         },
    //         error => {
    //              if (error.status  === 401) {
    //                 console.error("401 error");
    //                 this.util.redirectToLogin();
    //                 return Observable.empty();
    //             } else {
    //                 return Observable.throw(error);
    //             }             

    //         }
    //     );
        //  return observable.catch((err, source) => {
        //     if (err.status  === 401) {
        //             console.error("401 error");
        //         //  this.navigator.navigateToLogin({ redirectPath: "sections" });
        //             this.util.redirectToLogin();
        //             return Observable.empty();
        //         } else {
        //             return Observable.throw(err);
        //         }
        // });

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
            if (error.status  === 401) {
                console.error("401 error");
                this.util.redirectToLogin();
                return Observable.empty();
            } else {
                return Observable.throw(error);
            }
    //    return Observable.throw(error);
    }

    private onSubscribeSuccess(res: Response): void {
       this.total = this.total + 1;
        if(this.total >= this.sent * 2){
            this.util.setLoaderState(false);
        }
    }

    private onSubscribeError(error: any): void {
        this.total = this.total + 1;
        if(this.total >= this.sent * 2){
            this.util.setLoaderState(false);
        }
     //   console.error(error);
    }


}