package com.inventory.InventoryManagement.Repositary;

import com.inventory.InventoryManagement.Models.Supplier;
import com.inventory.InventoryManagement.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierRepository extends JpaRepository<Supplier,Integer> {

    Supplier readBySupplierEmail(String supplierEmail);
}
