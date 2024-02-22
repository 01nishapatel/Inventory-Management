package com.inventory.InventoryManagement.Repositary;

import com.inventory.InventoryManagement.Models.CompanyDetails;
import com.inventory.InventoryManagement.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyDetailsRepository extends JpaRepository<CompanyDetails,Integer> {
    CompanyDetails findByusers(User user);
    //CompanyDetailsRepository findBylogoName(String logoName);
}
