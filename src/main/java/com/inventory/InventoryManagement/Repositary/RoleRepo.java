package com.inventory.InventoryManagement.Repositary;

import com.inventory.InventoryManagement.Models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepo extends JpaRepository<Role,Integer> {
}
