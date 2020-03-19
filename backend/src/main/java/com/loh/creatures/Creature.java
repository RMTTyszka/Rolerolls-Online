package com.loh.creatures;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.loh.combat.AttackDetails;
import com.loh.combat.AttackService;
import com.loh.creatures.heroes.equipment.Equipment;
import com.loh.creatures.heroes.inventory.Inventory;
import com.loh.dev.Loh;
import com.loh.items.ItemInstanceRepository;
import com.loh.race.Race;
import com.loh.role.Role;
import com.loh.shared.Bonus;
import com.loh.shared.Entity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@javax.persistence.Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "CreatureType")
public class Creature extends Entity {

    public Creature() {
        this.attacker = new Attacker(this);
    }

    @Embedded
    @Getter @Setter
    @AttributeOverrides
            ({
                    @AttributeOverride(name="strength", column = @Column(name="base_strength") ),
                    @AttributeOverride(name="agility", column = @Column(name="base_agility") ),
                    @AttributeOverride(name="vitality", column = @Column(name="base_vitality") ),
                    @AttributeOverride(name="wisdom", column = @Column(name="base_wisdom") ),
                    @AttributeOverride(name="intuition", column = @Column(name="base_intuition") ),
                    @AttributeOverride(name="charisma", column = @Column(name="base_charisma") )})
    protected Attributes baseAttributes;

    @Embedded
    @Getter @Setter
    @AttributeOverrides
            ({
                    @AttributeOverride(name="strength", column = @Column(name="bonus_strength") ),
                    @AttributeOverride(name="agility", column = @Column(name="bonus_agility") ),
                    @AttributeOverride(name="vitality", column = @Column(name="bonus_vitality") ),
                    @AttributeOverride(name="wisdom", column = @Column(name="bonus_wisdom") ),
                    @AttributeOverride(name="intuition", column = @Column(name="bonus_intuition") ),
                    @AttributeOverride(name="charisma", column = @Column(name="bonus_charisma") )})
    protected Attributes bonusAttributes;

    @Transient
    protected Attributes totalAttributes;
    protected Attributes getTotalAttributes(){
        return bonusAttributes.GetSumOfAttributes(baseAttributes);
    }

    @Getter @Setter
    protected Integer level;

    @Getter @Setter
    protected String name;

    @Getter @Setter @ManyToOne
    protected Race race;

    @Getter @Setter @ManyToOne
    protected Role role;

    @Getter @Setter @OneToOne
    protected Equipment equipment;

    @Getter @Setter @OneToOne
    protected Inventory inventory;

    @ElementCollection
    @CollectionTable()
    @Getter
    @Setter
    private List<Bonus> bonuses = new ArrayList<>();

    public Integer getHit() {
        return equipment.getDefense() + getAttributeLevel(Attributes.Vitality);
    }
    public Integer getDefense() {
        return equipment.getDefense() + getAttributeLevel(Attributes.Vitality);
    }
    public Integer getEvasion() {
        return 10 + equipment.getEvasion() +  getAttributeLevel(Attributes.Agility);
    }
    public Integer getDodge() {
        return equipment.getDodge();
    }
    public Integer getLife() {
        return 5 + 4 * level +  (level  + 2) * getAttributeLevel(Attributes.Vitality);
    }
    public Integer getMoral() {
        return 5 + 4 * level +  (level  + 2) * getAttributeLevel(Attributes.Vitality) / 2;
    }

    public WeaponAttributes getMainWeaponAttributes() {
        return new WeaponAttributes(
                equipment.getMainWeaponGripType(),
                getAttributeLevel(equipment.getMainWeapon().getWeaponModel().getBaseWeapon().getDamageAttribute()),
                getAttributeLevel(equipment.getMainWeapon().getWeaponModel().getBaseWeapon().getHitAttribute()),
                equipment.getMainWeapon().getBonus());
    }
    public WeaponAttributes getOffWeaponAttributes() {
        return equipment.getOffWeapon() != null ? new WeaponAttributes(
                equipment.getOffWeaponGridType(),
                getAttributeLevel(equipment.getOffWeapon().getWeaponModel().getBaseWeapon().getDamageAttribute()),
                getAttributeLevel(equipment.getOffWeapon().getWeaponModel().getBaseWeapon().getHitAttribute()),
                equipment.getOffWeapon().getBonus()) : null;
    }
    @Getter @Setter @Transient @JsonIgnore
    protected Attacker attacker;

    public Integer getAttributePoints(String attr) {
        return
                baseAttributes.getAttributePoints(attr)
                + bonusAttributes.getAttributePoints(attr)
                + race.getAttributePoints(attr)
                + role.getAttributePoints(attr)
                + bonuses.stream().filter(bonus -> bonus.getProperty() == attr).map(e -> e.getLevel()).reduce(0 , (a ,b) -> a + b).intValue();
    }

    public Integer getAttributeLevel(String attr) {
        return Loh.getLevel(getAttributePoints(attr));
    }

    @Transient
    protected Integer totalAttributesInitialPoints;
    public Integer getTotalAttributesInitialPoints(){
        return 8 + 6 + 4 + 2 + 2 + 8*6;
    }

    @Transient
    protected Integer maxInitialAttributePoints;
    public Integer getMaxInitialAttributePoints(){
        return 8+6;
    }
    @Transient
    protected Integer maxAttributeBonusPoints;
    public Integer getMaxAttributeBonusPoints(){
        return level - 1;
    }
    @Transient
    protected Integer totalAttributesBonusPoints;
    public Integer getTotalAttributesBonusPoints(){
        return (level - 1) * 2;
    }
    @Transient
    protected Integer expToNextLevel;
    public Integer getExpToNextLevel(){
        return calculateExpToNextLevel(level);
    }

    public static Integer getExpToNextLevel(Integer level){
        return calculateExpToNextLevel(level);
    }
    public static Integer calculateExpToNextLevel(Integer level) {
        if (level > 1) {
            return calculateExpToNextLevel(level - 1) +
                    5 * x(level);
        } else {
            return 500;
        }
    }
    public static Integer x (Integer level) {
        if (level > 1) {
            return x(level - 1) + 50 * (level - 1);
        } else {
            return 100;
        }
    }


    public AttackDetails fullAttack(Creature target, AttackService service) {
        return service.fullAttack(this, target);
    }

    public void levelUpforTest(CreatureRepository creatureRepository, ItemInstanceRepository weaponInstanceRepository) {
        level++;
        bonusAttributes.levelUp(Attributes.Agility);
        bonusAttributes.levelUp(Attributes.Strength);
        bonusAttributes.levelUp(Attributes.Vitality);
        bonusAttributes.levelUp(Attributes.Charisma);
        bonusAttributes.levelUp(Attributes.Intuition);
        bonusAttributes.levelUp(Attributes.Wisdom);
        equipment.getMainWeapon().levelUp(weaponInstanceRepository);
        weaponInstanceRepository.save(equipment.getMainWeapon());
        if (equipment.getOffWeapon() != null) {
            equipment.getOffWeapon().levelUp(weaponInstanceRepository);
        }
        equipment.getArmor().levelUp(weaponInstanceRepository);
        if (level % 5 == 0) {
            bonuses.add(new Bonus(Attributes.Strength, 1, 0));
            bonuses.add(new Bonus(Attributes.Agility, 1, 0));
            bonuses.add(new Bonus(Attributes.Vitality, 1, 0));
            bonuses.add(new Bonus(Attributes.Wisdom, 1, 0));
            bonuses.add(new Bonus(Attributes.Intuition, 1, 0));
            bonuses.add(new Bonus(Attributes.Charisma, 1, 0));
        }
        creatureRepository.save(this);

    }
    public void levelUp(List<String> attributesToLevel) {
        level++;
        for (String attribute: attributesToLevel) {
            bonusAttributes.levelUp(attribute);
        }
    }

    public Integer getInateLevelBonus(Integer attributePoints) {
        return (attributePoints - 5) / 5 * 2;
    }



}
