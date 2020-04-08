import {Component, Input, OnInit} from '@angular/core';
import {FormGroup} from '@angular/forms';
import {createForm} from '../../shared/EditorExtension';
import {Equipment} from '../../shared/models/Equipment.model';
import {Inventory} from '../../shared/models/Inventory.model';
import {ArmorInstance} from '../../shared/models/ArmorInstance.model';
import {WeaponInstance} from '../../shared/models/WeaponInstance.model';
import {GloveInstance} from '../../shared/models/GloveInstance.model';
import {BeltInstance} from '../../shared/models/BeltInstance.model';
import {HeadpieceInstance} from '../../shared/models/HeadpieceInstance.model';
import {NeckAccessoryInstance} from '../../shared/models/NeckAccessoryInstance.model';
import {RingHand, SelectedRing} from '../../creatures-shared/inventory/inventory-ring-select/inventory-ring-select.component';
import {RingInstance} from '../../shared/models/RingInstance.model';

@Component({
  selector: 'loh-equipment',
  templateUrl: './equipment.component.html',
  styleUrls: ['./equipment.component.css']
})
export class EquipmentComponent implements OnInit {

  @Input() form: FormGroup = this.createForm();
  @Input() controlName = 'equipment';
  @Input() inventoryControlName = 'inventory';
  ringHand = RingHand;
  entity: Equipment;
  constructor() { }

  ngOnInit() {
    this.entity = this.getEntity();
  }

  createForm() {
    const form = new FormGroup({});
    const entity = new Equipment();
    createForm(form, entity);
    return form;
  }

  getEntity() {
    const entity = this.form.get(this.controlName).value;
    return entity ? entity : new Equipment();
  }
  get inventory(): Inventory {
    return this.form.get(this.inventoryControlName).value;
  }
  get equipment(): Equipment {
    return this.form.get(this.controlName).value;
  }

  findItem(id: string) {
    return this.inventory.items.find(r => r.id === id);
  }

  armorSelected(armor: ArmorInstance) {
    const selectedArmor = this.findItem(armor.id);
    const removedArmor = this.equipment.armor;
    const armorForm = new FormGroup({});
    createForm(armorForm , selectedArmor);
    this.equipment.armor = armorForm.getRawValue() as ArmorInstance;
    this.inventory.items.splice(this.inventory.items.indexOf(selectedArmor), 1);
  }
  mainWeaponSelected(weapon: WeaponInstance) {
    const selectedWeapon = this.findItem(weapon.id);
    const removedWeapon = this.equipment.mainWeapon;
    const weaponForm = new FormGroup({});
    createForm(weaponForm , selectedWeapon);
    this.equipment.mainWeapon = weaponForm.getRawValue() as WeaponInstance;
    this.inventory.items.splice(this.inventory.items.indexOf(selectedWeapon), 1);
  }
  glovesSelected(gloves: GloveInstance) {
    const selectedGloves = this.findItem(gloves.id);
    const removedGloves = this.equipment.gloves;
    const glovesForm = new FormGroup({});
    createForm(glovesForm , selectedGloves);
    this.equipment.gloves = glovesForm.getRawValue() as GloveInstance;
    this.inventory.items.splice(this.inventory.items.indexOf(selectedGloves), 1);
  }
  beltSelected(belt: BeltInstance) {
    const selectedBelt = this.findItem(belt.id);
    const removedBelt = this.equipment.belt;
    const beltForm = new FormGroup({});
    createForm(beltForm , selectedBelt);
    this.equipment.belt = beltForm.getRawValue() as BeltInstance;
    this.inventory.items.splice(this.inventory.items.indexOf(selectedBelt), 1);
  }
  headpieceSelected(belt: HeadpieceInstance) {
    const selectedHeadpiece = this.findItem(belt.id);
    const removedHeadpiece = this.equipment.headpiece;
    const headpieceForm = new FormGroup({});
    createForm(headpieceForm , selectedHeadpiece);
    this.equipment.headpiece = headpieceForm.getRawValue() as HeadpieceInstance;
    this.inventory.items.splice(this.inventory.items.indexOf(selectedHeadpiece), 1);
  }
  neckAccessorySelected(belt: NeckAccessoryInstance) {
    const selectedItem = this.findItem(belt.id);
    const removedItem = this.equipment.headpiece;
    const formGroup = new FormGroup({});
    createForm(formGroup , selectedItem);
    this.equipment.neckAccessory = formGroup.getRawValue() as NeckAccessoryInstance;
    this.inventory.items.splice(this.inventory.items.indexOf(selectedItem), 1);
  }
  ringSelected(selectedRing: SelectedRing) {
    const selectedItem = this.findItem(selectedRing.ring.id);
    let removedItem: RingInstance;
    if (selectedRing.isRight) {
      removedItem = this.equipment.ringRight;
    } else if (selectedRing.isLeft) {
      removedItem = this.equipment.ringLeft;
    }
    const formGroup = new FormGroup({});
    createForm(formGroup , selectedItem);
    if (selectedRing.isRight) {
      this.equipment.ringRight = formGroup.getRawValue() as RingInstance;
    } else if (selectedRing.isLeft) {
      this.equipment.ringLeft = formGroup.getRawValue() as RingInstance;
    }
    this.inventory.items.splice(this.inventory.items.indexOf(selectedItem), 1);
  }

}
