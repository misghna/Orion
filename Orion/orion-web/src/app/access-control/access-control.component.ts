import { Component, OnInit } from '@angular/core';
import { UserService } from '../users/users.service';
import { UtilService } from '../service/util.service';
import { AccessControlService } from './access-control.service';

@Component({
  selector: 'app-access-control',
  templateUrl: './access-control.component.html',
  styleUrls: ['./access-control.component.css']
})
export class AccessControlComponent implements OnInit {

// google maps zoom level
  zoom: number = 2;
  
  // initial center position for the map
  lat: number = 51.673858;
  lng: number = 7.815982;
  access_details;
	 markers: marker[] = []

allUsers;activeUser;access;
constructor(private userService: UserService,private utilService: UtilService,
						private accessControl : AccessControlService) {
}


  ngOnInit() {
    this.userService.getAllUsers()
    .subscribe(
        response => {    
            this.allUsers = response;     
        },
        error => {
          console.error(error);
          return {};
        }
      );
  }


loadAcces(userName){
    this.accessControl.getAccessHistory(userName)
    .subscribe(
        response => {    
            this.access = response;
						this.createAccessObject(response);
        },
        error => {
          this.markers = []
        }
      );
}

createAccessObject(response){
	var markerList = [];
	response.forEach(ac => {
			var acObj = {'lat':ac.lat,'lng':ac.lng,'label':ac.fullName,'draggable':false};
			markerList.push(acObj);
	});
	this.markers = markerList;
}


  clickedMarker(label: string, index: number) {
 //   console.log(`clicked the marker: ${label || index}`)
    this.access_details = label + "\n" + "date - blabla hadhjsoa lashdoa";
  }
  
  markerDragEnd(m: marker, $event: MouseEvent) {
  //  console.log('dragEnd', m, $event);
  }
  
 



}

interface marker {
	lat: number;
	lng: number;
	label?: string;
	draggable: boolean;
}