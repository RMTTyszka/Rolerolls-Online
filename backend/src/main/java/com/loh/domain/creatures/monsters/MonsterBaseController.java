package com.loh.domain.creatures.monsters;

import com.loh.domain.creatures.monsters.models.MonsterModel;
import com.loh.domain.creatures.monsters.models.MonsterModelRepository;
import com.loh.shared.BaseCrudController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

@CrossOrigin
@Controller    // This means that this class is a Controller
@RequestMapping(path="/monsters/model",  produces = "application/json; charset=UTF-8")
public class MonsterBaseController extends BaseCrudController<MonsterModel,MonsterModel, MonsterModel, MonsterModelRepository> {


	public MonsterBaseController(MonsterModelRepository repository) {
		super(repository);
	}

	@Override
	public MonsterModel getNew() {
		return new MonsterModel();
	}

	@Override
	protected MonsterModel createInputToEntity(MonsterModel monsterModel) {
		return monsterModel;
	}

	@Override
	protected MonsterModel updateInputToEntity(MonsterModel monsterModel) {
		return monsterModel;
	}
}
