package com.loh.items.equipable.rings.head;

import com.loh.items.equipable.rings.head.ringModels.RingModel;
import com.loh.items.equipable.rings.head.ringModels.RingModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RingSeeder {

    @Autowired
    private RingModelRepository ringModelRepository;

    public void seed() {

        for(String ringName : DefaultRings.getList()) {
            if (ringModelRepository.getByNameAndSystemDefaultTrue("Common " + ringName) == null) {
                RingModel ringModel = new RingModel(ringName);
                ringModel.setSystemDefault(true);
                ringModelRepository.save(ringModel);
            }
        }
        if (ringModelRepository.getByNameAndSystemDefaultTrue(DefaultRings.DummyRing) == null) {
            RingModel ringModel = new RingModel(DefaultRings.DummyRing);
            ringModel.setSystemDefault(true);
            ringModelRepository.save(ringModel);
        }
        if (ringModelRepository.getByNameAndSystemDefaultTrue(DefaultRings.NoRing) == null) {
            RingModel ringModel = new RingModel(DefaultRings.NoRing);
            ringModel.setSystemDefault(true);
            ringModelRepository.save(ringModel);
        }
    }
}
