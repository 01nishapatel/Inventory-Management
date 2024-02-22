package com.inventory.InventoryManagement.service;

import com.inventory.InventoryManagement.Models.Customer;
import com.inventory.InventoryManagement.Models.Order;
import com.inventory.InventoryManagement.Models.OrderDetail;
import com.inventory.InventoryManagement.Models.Product;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Optional;
import java.util.Set;

public interface Orderservice {
    Order addItem(Product product, int Quntity, Customer customer,int orderid);

}
