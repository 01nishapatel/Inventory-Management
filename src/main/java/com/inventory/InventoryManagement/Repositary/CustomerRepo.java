package com.inventory.InventoryManagement.Repositary;

import com.inventory.InventoryManagement.Models.Customer;
import com.inventory.InventoryManagement.Models.OrderDetail;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface CustomerRepo extends JpaRepository<Customer,Integer> {

Customer findByEmail(String email);

Customer findByPhonenumber(String phonenumber);

}
