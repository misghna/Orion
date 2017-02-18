import { Component, OnInit } from '@angular/core';

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
    ngOnInit() { }

  clickedMarker(label: string, index: number) {
    console.log(`clicked the marker: ${label || index}`)
    this.access_details = label + "\n" + "date - blabla hadhjsoa lashdoa";
  }
  
  markerDragEnd(m: marker, $event: MouseEvent) {
    console.log('dragEnd', m, $event);
  }
  
  markers: marker[] = [
	  {
		  lat: 51.673858,
		  lng: 7.815982,
		  label: 'Misghna',
		  draggable: true
	  },
	  {
		  lat: -1.723858,
		  lng: 7.895982,
		  label: 'Macconen',
		  draggable: false
	  },
	  {
		  lat: 51.723858,
		  lng: 7.895982,
		  label: 'C',
		  draggable: true
	  }
  ]



  constructor() { }

}

interface marker {
	lat: number;
	lng: number;
	label?: string;
	draggable: boolean;
}