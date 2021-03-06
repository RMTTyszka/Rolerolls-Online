import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {LOH_API} from '../loh.api';
import {CreatureRollResult} from '../shared/models/rolls/CreatureRollResult';
import {Subject} from 'rxjs';
import {DCChance} from '../shared/models/rolls/DCChanceResult';

@Injectable({
  providedIn: 'root'
})
export class CreatureRollsService {
  private _creatureRolled = new Subject<CreatureRollResult>();
  serverUrl = LOH_API.myBackUrl
  path = 'creatures-rolls'
  constructor(
    private httpClient: HttpClient
  ) { }

  roll(creatureId: string, property: string, difficulty: number, complexity: number) {
    let params: HttpParams = new HttpParams();
    if (difficulty) {
      params = params.set('difficulty', difficulty.toString());
    }
    if (complexity) {
      params = params.set('complexity', complexity.toString());
    }
    params = params.set('property', property)
    return this.httpClient.get<CreatureRollResult>(this.serverUrl + this.path + `/creatures/${creatureId}`, {
      params: params
    });
  }
  getChances(creatureId: string, property: string, requiredChance: number) {
    const params = new HttpParams().set('property', property).set('requiredChance', requiredChance.toString());
    return this.httpClient.get<DCChance[]>(this.serverUrl + this.path + `/creatures/${creatureId}/chances`, {
      params: params
    });
  }

  creatureRolled() {
    return this._creatureRolled.asObservable();
  }
  emitCreatureRolled(rollResult: CreatureRollResult) {
    this._creatureRolled.next(rollResult);
  }

}
