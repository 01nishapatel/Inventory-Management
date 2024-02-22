package com.inventory.InventoryManagement.Repositary;

import com.inventory.InventoryManagement.Models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepo extends JpaRepository<Category,Integer> {
}
