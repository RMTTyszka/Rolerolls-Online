import {Entity} from './Entity.model';

export class ItemInstance extends Entity {
  public level = 1;
  public value = 0;
  public quantity = 1;
  public name = '';
  public equipable = false;
}
