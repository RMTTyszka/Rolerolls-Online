import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MonstersListComponent } from './monster-list/monsters-list.component';
import { Routes, RouterModule } from '@angular/router';
import { SharedModule } from '../shared/shared.module';
import { MonsterModelComponent } from './monsters-bases/monster-model-editor/monster-model.component';
import { MonsterComponent } from './monster/monster.component';
import { MonsterBaseListComponent } from './monsters-bases/monster-model-list/monster-model-list.component';
import { MonstersComponent } from './monsters.component';
import { RaceSharedModule } from '../races/shared/race-shared.module';
import { RolesSharedModule } from '../roles/roles-shared/roles-shared.module';
import { MonstersSharedModule } from './monsters-shared/monsters-shared.module';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MonsterBaseService } from './monsters-bases/monster-model-editor/monster-model.service';


const routes: Routes = [
  {path: '', component: MonstersComponent},
  {path: 'model/:id', component: MonsterModelComponent, resolve: { monsterBase: MonsterBaseService}},
  {path: 'model', component: MonsterModelComponent, resolve: { monsterBase: MonsterBaseService}}
];

@NgModule({
  imports: [
    CommonModule,
    RouterModule.forChild(routes),
    SharedModule,


    RaceSharedModule,
    RolesSharedModule,
    MonstersSharedModule

  ],
  declarations: [MonstersListComponent, MonsterComponent, MonsterModelComponent, MonsterBaseListComponent, MonstersComponent],
  providers: [
    { provide: MatDialogRef, useValue: {} },
    { provide: MAT_DIALOG_DATA, useValue: {}}
  ],
  entryComponents: [
    MonsterComponent, MonsterModelComponent
  ]
})
export class MonstersModule { }
