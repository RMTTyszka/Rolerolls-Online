import {Injectable, Injector} from '@angular/core';
import {LegacyBaseCrudServiceComponent} from 'src/app/shared/legacy-base-service/legacy-base-crud-service.component';
import {MonsterModel} from 'src/app/shared/models/creatures/monsters/MonsterModel.model';

@Injectable({
  providedIn: 'root'
})
export class LegacyMonstersBaseService extends LegacyBaseCrudServiceComponent<MonsterModel> {
  path = 'monsterBase';
  constructor(injector: Injector) {
    super(injector);
   }
}
