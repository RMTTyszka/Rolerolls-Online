package com.loh.creatures.heroes.inventory;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface InventoryRepository extends CrudRepository<Inventory, UUID>, JpaSpecificationExecutor<Inventory> {
}