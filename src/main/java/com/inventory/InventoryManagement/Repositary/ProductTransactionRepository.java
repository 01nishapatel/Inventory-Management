package com.inventory.InventoryManagement.Repositary;

import com.inventory.InventoryManagement.Models.ProductTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductTransactionRepository extends JpaRepository<ProductTransaction,Integer> {


//    List<Product> AllProductBySupplier(int SupplierID);
}
