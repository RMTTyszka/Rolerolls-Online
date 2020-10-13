import {BaseComponentConfig} from '../shared/components/base-component-config';
import {RRColumns} from '../shared/components/rr-grid/r-r-grid.component';
import {RaceEditorComponent} from './race-editor/race-editor.component';

export class RaceConfig implements BaseComponentConfig {
  editorTitle = 'Race';
  editor = RaceEditorComponent;
  creator = RaceEditorComponent;
  entityListColumns: RRColumns[] = [
    {
      header: 'Race',
      property: 'name'
    }
  ];
  fieldName: string;
  selectModalColumns: RRColumns[] = [
    {
      header: 'Race',
      property: 'name'
    }
  ];
  selectModalTitle = 'Races';
  selectPlaceholder = 'Races';
  path = 'races';
}
