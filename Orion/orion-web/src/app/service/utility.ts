import { isDevMode} from '@angular/core';

export default class Utils {
    static doSomething(val: string) { console.log('emmm it works'); }
    static doSomethingElse(val: string) { return val; }

    static getBaseUrl(){
      if(isDevMode()){
        return 'http://localhost:8080/'; 
      }else{
        return '';
      }
    }
}