package com.inventory.InventoryManagement.Repositary;

import com.inventory.InventoryManagement.Models.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface OrderDetailRepo extends JpaRepository<OrderDetail,Integer> {


}
