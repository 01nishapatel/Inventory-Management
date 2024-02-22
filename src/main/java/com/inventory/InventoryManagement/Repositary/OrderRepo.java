package com.inventory.InventoryManagement.Repositary;

import com.inventory.InventoryManagement.Models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepo extends JpaRepository<Order,Integer> {

    @Query("SELECT c FROM Order c ORDER BY OrderID DESC")
    List<Order> findWithOrder();

}
