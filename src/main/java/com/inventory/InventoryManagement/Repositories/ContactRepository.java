package com.inventory.InventoryManagement.Repositories;

import com.inventory.InventoryManagement.Models.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ContactRepository extends JpaRepository<Contact,Integer> {
}
