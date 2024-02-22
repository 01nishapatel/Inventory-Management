package com.inventory.InventoryManagement.Services;

import com.inventory.InventoryManagement.Models.Product;
import com.inventory.InventoryManagement.Repositary.ProductRepo;
import com.inventory.InventoryManagement.Repositary.ProductTransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ProductTransactionService {

//    @Autowired
//    private ProductTransactionRepository ptr;
//    @Autowired
//    private ProductRepo pro;

//    public List<Product> getProductListBySupplier(int id){
//        List<Product> p=new ArrayList<>();
//        p=pro.findAll().stream().findAny(id);
//
//    return p;
//    }
}
