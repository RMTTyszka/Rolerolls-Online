package com.loh.items;

import com.loh.items.armors.*;
import com.loh.items.armors.armorCategories.ArmorCategory;
import com.loh.items.armors.armorCategories.ArmorCategoryEnum;
import com.loh.items.armors.armorCategories.ArmorCategoryRepository;
import com.loh.items.armors.armorModel.ArmorModel;
import com.loh.items.armors.armorModel.ArmorModelRepository;
import com.loh.items.armors.armorTypes.ArmorType;
import com.loh.items.armors.baseArmor.BaseArmor;
import com.loh.items.armors.baseArmor.BaseArmorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
@Component
public class EquipmentSeeder {

	
	@Autowired
	private ArmorModelRepository armorRepo;
	@Autowired
	private ArmorRepository armorInstances;
	@Autowired
	private ArmorCategoryRepository armorCategoryRepository;
	@Autowired
	private BaseArmorRepository baseArmorRepository;
	
	@EventListener
	public void seed(ContextRefreshedEvent event) {
		CreateArmors();
	}
	
	private void CreateArmors()
	{
/*		if (armorRepo.count() <= 0) {
			ArmorModel newArmor = new ArmorModel();
			newArmor.name = ("Full plate");
			armorRepo.save(newArmor);
		}*/
		if (armorCategoryRepository.count() <= 0) {
			ArmorCategory lightArmor = new ArmorCategory(ArmorCategoryEnum.Light, ArmorType.Light, 1, 1, 1);
			ArmorCategory mediumArmor = new ArmorCategory(ArmorCategoryEnum.Medium, ArmorType.Medium, 2, 0, 3);
			ArmorCategory heavyArmor = new ArmorCategory(ArmorCategoryEnum.Heavy, ArmorType.Heavy, 3, -1, 4);
			ArmorCategory noArmor = new ArmorCategory(ArmorCategoryEnum.None, ArmorType.None, 0, 1, 0);
			armorCategoryRepository.save(lightArmor);
			armorCategoryRepository.save(mediumArmor);
			armorCategoryRepository.save(heavyArmor);
			armorCategoryRepository.save(noArmor);
		}
		if (baseArmorRepository.count() <= 0){
			ArmorCategory heavy = armorCategoryRepository.findArmorCategoryByArmorCategoryEnum(ArmorCategoryEnum.Heavy);
			ArmorCategory medium = armorCategoryRepository.findArmorCategoryByArmorCategoryEnum(ArmorCategoryEnum.Medium);
			ArmorCategory light = armorCategoryRepository.findArmorCategoryByArmorCategoryEnum(ArmorCategoryEnum.Light);
			ArmorCategory noneArmor = armorCategoryRepository.findArmorCategoryByArmorCategoryEnum(ArmorCategoryEnum.None);
			BaseArmor fullPlate = BaseArmor.DefaultBaseArmor("Full Plate", heavy);
			BaseArmor chainMail = BaseArmor.DefaultBaseArmor("Chain Mail", medium);
			BaseArmor leatherArmor = BaseArmor.DefaultBaseArmor("Leather Armor", light);
			BaseArmor empty = BaseArmor.DefaultBaseArmor("None Armor", noneArmor);
			baseArmorRepository.save(fullPlate);
			baseArmorRepository.save(chainMail);
			baseArmorRepository.save(leatherArmor);
			baseArmorRepository.save(empty);
		}
		if (armorRepo.count() <= 0){
			BaseArmor baseNoneArmor = baseArmorRepository.findByCategory_ArmorCategoryEnum(ArmorCategoryEnum.None);
			ArmorModel noneArmor = new ArmorModel();
			noneArmor.setStatic(true);
			noneArmor.setBaseArmor(baseNoneArmor);
			noneArmor.setName("None Armor");
			armorRepo.save(noneArmor);
		}
		if (armorInstances.findByArmorModel_BaseArmor_Category_ArmorCategoryEnum(ArmorCategoryEnum.None) == null) {
			ArmorInstance armor = new ArmorInstance();
			ArmorModel noArmor = armorRepo.findArmorByBaseArmor_Category_ArmorCategoryEnum(ArmorCategoryEnum.None);
			armor.setArmorModel(noArmor);
			armor.setName(noArmor.name);
			armorInstances.save(armor);
		}


	}
}