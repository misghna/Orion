import { Pipe, PipeTransform } from '@angular/core';


@Pipe({ name: 'dateDiff' })
export class DateDiffPipe implements PipeTransform {
 transform(value: string, optionalArg: String []) {
        var today = new Date();

        return Math.floor((parseInt(value) - today.getTime())/24/3600000);

 }

} 