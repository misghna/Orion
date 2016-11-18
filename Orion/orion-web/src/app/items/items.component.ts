import { Component, OnInit } from '@angular/core';
import { MiscService } from '../service/misc.service';

@Component({
  selector: 'app-items',
  templateUrl: './items.component.html',
  styleUrls: ['./items.component.css']
})
export class ItemsComponent implements OnInit {
  data;
  hideLoader;
  filterQuery = "";

  constructor(private miscService:MiscService) {
    this.hideLoader=true;
   }

  ngOnInit() {
  }


  loadItemsTable(){
      this.miscService.getAllItems()
      .subscribe(
          response => {
            console.log(response);
              this.data = response;     
          },
          error => {
            console.error(error);
            return {};
          }
        );
    }
}
