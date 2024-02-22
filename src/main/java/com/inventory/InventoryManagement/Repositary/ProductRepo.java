package com.inventory.InventoryManagement.Repositary;

import com.inventory.InventoryManagement.Models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepo extends JpaRepository<Product,Integer> {

    List<Product> findByproductQuantityLessThan(int productQuantity);
}
