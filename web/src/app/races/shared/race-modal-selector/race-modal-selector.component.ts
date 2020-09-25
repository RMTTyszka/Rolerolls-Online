import {Component, Injector, OnInit} from '@angular/core';
import {Race} from 'src/app/shared/models/Race.model';
import {LegacyBaseSelectorComponent} from 'src/app/shared/legacy-base-selector/legacy-base-selector.component';
import {RacesService} from '../../races.service';
import {MatDialogRef} from '@angular/material/dialog';
import {Router} from '@angular/router';

@Component({
  selector: 'loh-race-selector',
  templateUrl: './race-modal-selector.component.html',
  styleUrls: ['./race-modal-selector.component.css']
})
export class RaceModalSelectorComponent extends LegacyBaseSelectorComponent<Race> implements OnInit {


  constructor(injector: Injector,
    protected dialogRef: MatDialogRef<RaceModalSelectorComponent>,
    service: RacesService,
    protected router: Router) {
    super(injector, service);
   }

  ngOnInit() {
    this.getAll();
  }

}
