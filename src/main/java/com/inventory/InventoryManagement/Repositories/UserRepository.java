package com.inventory.InventoryManagement.Repositories;

import com.inventory.InventoryManagement.Models.Role;
import com.inventory.InventoryManagement.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

    User readByEmail(String Email);

    User findByEmail(String email);

    User findByResetPasswordToken(String token);

    List<User> findByRoleID(Role role);

    List<User> findByParentUserID(int id);

    @Query("SELECT c FROM User c WHERE c.UserName=:name")
    User finddByUserName(String name);
}
