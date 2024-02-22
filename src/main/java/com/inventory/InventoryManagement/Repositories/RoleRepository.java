package com.inventory.InventoryManagement.Repositories;

import com.inventory.InventoryManagement.Models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role,Integer> {
    Role findRoleNameByRoleID(int Role);

    Role findByRoleName(String name);
}
