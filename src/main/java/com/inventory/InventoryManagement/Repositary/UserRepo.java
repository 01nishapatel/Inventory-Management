package com.inventory.InventoryManagement.Repositary;

import com.inventory.InventoryManagement.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepo extends JpaRepository<User,Integer> {

    List<User> findByparentUserID(int ParentUserID);
}
