import { Pipe, PipeTransform } from '@angular/core';


@Pipe({ name: 'filterName' })
export class FilterNamePipe implements PipeTransform {
 transform(value: string, optionalArg: String) {
     if(value!=null && value.indexOf("-")>0){
         return value.split("-")[1];
     }else{
         return value;
     }
 }

} 