package com.loh.combat;

import com.loh.items.weapons.WeaponModel;
import com.loh.shared.OldCreature;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@CrossOrigin
@Controller    // This means that this class is a Controller
@RequestMapping(path="/combat",  produces = "application/json; charset=UTF-8")
public class CombatController {
	
	

	protected Random random = new Random();

	public void fullAttack (OldCreature attacker, OldCreature target, CombatActionDto dto) throws NoSuchFieldException, SecurityException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
		WeaponModel mainWeapon = attacker.getEquipment().getMainWeapon().getWeapon();
		WeaponModel offWeapon = attacker.getEquipment().getOffWeapon().getWeapon();
		List<Integer> hitsMain = new ArrayList<>();
		List<Integer> mainRolls = new ArrayList<>();
		List<Integer> offRolls = new ArrayList<>();
		Integer mainBonus = attacker.getAttrMod(mainWeapon.getHitAttribute()) + mainWeapon.getBonus();
		Integer offBonus = 0;
		Integer evasion = target.evasion();
		if (mainWeapon.getNumberOfHands() > 0) {
			for (int x = 0; x < attacker.getAttrMod(mainWeapon.getHitAttribute()); x++) {
				int roll = random.nextInt(20) + 1;
				mainRolls.add(roll);
				if (roll == 20) { hitsMain.add(1); continue; }
				if (roll == 1) { hitsMain.add(-1); continue; }
				roll += mainBonus;
				if (roll >= evasion) hitsMain.add(0);
			}
		}
		List<Integer> hitsOff = new ArrayList<>();

		if (offWeapon != null && offWeapon.getNumberOfHands() == 1) {
			offBonus = attacker.getAttrMod(offWeapon.getHitAttribute()) + offWeapon.getBonus();
			for (int x = 0; x < attacker.getAttrMod(offWeapon.getHitAttribute()); x++) {
				int roll = random.nextInt(20) + 1;
				offRolls.add(roll);
				if (roll == 20) { hitsOff.add(1); continue; }
				if (roll == 1) { hitsOff.add(-1); continue; }
				roll += offBonus;
				if (roll >= evasion) hitsOff.add(0);
			}
		}
		dto.setMainWeaponHits(hitsMain);
		dto.setOffWeaponHits(hitsOff);
		dto.setEvasion(evasion);
		dto.setMainWeaponBonus(mainBonus);
		dto.setOffWeaponBonus(offBonus);
		dto.setMainWeaponRolls(mainRolls);
		dto.setOffWeaponRolls(offRolls);
	}
	
	@GetMapping(path="/fullAttack")
	public @ResponseBody CombatActionDto getFullAttack(Integer attackerId, Integer targetId) throws NoSuchFieldException, SecurityException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
/*		Monster attacker = monsterRepo.findById(attackerId).get();
		Monster target = monsterRepo.findById(targetId).get();
		CombatActionDto dto = new CombatActionDto();
		
		fullAttack(attacker, target, dto);
		return dto;*/
return null;
	}
	
	
}