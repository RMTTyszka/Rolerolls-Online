import { Component, OnInit, Injector, Inject } from '@angular/core';
import { BaseCreatorComponent } from 'src/app/shared/base-creator/base-creator.component';
import { Power, PowerCategory } from 'src/app/shared/models/Power.model';
import { PowerService } from '../power.service';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Race } from 'src/app/shared/models/Race.model';

@Component({
  selector: 'loh-power-editor',
  templateUrl: './power-editor.component.html',
  styleUrls: ['./power-editor.component.css']
})
export class PowerEditorComponent extends BaseCreatorComponent<Power> implements OnInit {

  powerCategories: string[];
  races: Race[] = [];

  constructor(
    injector: Injector,
    protected service: PowerService,
    protected dialogRef: MatDialogRef<PowerEditorComponent>,
    @Inject(MAT_DIALOG_DATA) data: any

  ) {
    super(injector, service);
    this.getEntity(data ? data.id : null);
   }

  ngOnInit() {
    const options = Object.keys(PowerCategory);
    this.powerCategories = options.slice(options.length / 2);

  }
  afterGetEntity() {
    if (this.action === 'update') {
      this.service.getPowerUsages(this.entity.id).subscribe(races => {
        this.races = races;
      });
    }
  }



}
