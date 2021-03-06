import {Entity} from './models/Entity.model';
import {EventEmitter, Injector} from '@angular/core';
import {LegacyBaseCrudServiceComponent} from './legacy-base-service/legacy-base-crud-service.component';
import {Observable, Subject} from 'rxjs';
import {finalize, tap} from 'rxjs/operators';

export class BaseEntityService<T extends Entity> extends LegacyBaseCrudServiceComponent<T> {

  id: string;
  entity: T;
  onSaveAction = new EventEmitter<boolean>();
  onEntityChange = new Subject<T>();
  constructor(injector: Injector, public type: new () => T
    ) {
    super(injector);
  }

  getEntity(id: string): Observable<T> {
    return this.get(id).pipe(
      tap(entity => {
        console.log(entity);
        this.entity = entity;
        this.onEntityChange.next(this.entity);
      }
    ),
      finalize(() => {
      }));
  }

  getNewEntity() {
    return this.http.get<T>(this.serverUrl + this.path + '/getNew').pipe(
      tap(entity => {
          console.log(entity);
          this.entity = entity;
        }
      ),
      finalize(() => {
        this.onEntityChange.next(this.entity);
      }));
  }
}
