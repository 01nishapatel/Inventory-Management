package com.inventory.InventoryManagement.Repositary;

import com.inventory.InventoryManagement.Models.Tax;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TaxRepo extends JpaRepository<Tax,Integer> {
}
