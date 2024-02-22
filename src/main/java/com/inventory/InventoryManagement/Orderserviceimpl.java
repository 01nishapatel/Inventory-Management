package com.inventory.InventoryManagement;

import com.inventory.InventoryManagement.Models.*;
import com.inventory.InventoryManagement.Repositary.OrderDetailRepo;
import com.inventory.InventoryManagement.Repositary.OrderRepo;
import com.inventory.InventoryManagement.Repositary.ProductRepo;
import com.inventory.InventoryManagement.Repositary.ProductTransactionRepository;
import com.inventory.InventoryManagement.service.Orderservice;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.val;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Service
public class Orderserviceimpl implements Orderservice {

    int orderid;
    @Autowired
    private OrderDetailRepo itemRepository;

    @Autowired
    private OrderRepo cartRepository;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private ProductTransactionRepository ptr;
    Optional<Order> sc;


    private double totalprice(List<OrderDetail> orderitems) {
        double totalprice = 0.0;
        for (OrderDetail item : orderitems) {

            totalprice += item.getTotalamount();
        }
        return totalprice;
    }


//    private double totaltaxcalc(List<OrderDetail> orderitems) {
//        double totaltax = 0.0;
//        for (OrderDetail item : orderitems) {
//
//            totaltax += item.getProducts().categories.getTaxes().getRate();
//        }
//        return totaltax;
//    }

    @Override
    public Order addItem(Product product, int Quntity, Customer customer,int orderid) {
        Optional<Order> sc = cartRepository.findById(orderid);
        Order shoppingCart;
        if (sc.isEmpty()) {
            shoppingCart = new Order();
        }
        else
        {
            shoppingCart=sc.get();
        }


        List<OrderDetail> cartItemList = shoppingCart.getOrderDetails();
        OrderDetail cartItem = new OrderDetail();
        int itemQuantity = 0;
        if (cartItemList == null) {
            cartItemList = new ArrayList<>();

            cartItem = new OrderDetail();
            cartItem.setProducts(product);
            cartItem.setOrders(shoppingCart);
            cartItem.setQuantity(Quntity);

            float total = (float) (Quntity * product.getProductPrice());
            cartItem.setUnitPrice((float) total);
            Tax tax=product.getCategories().getTaxes();
            double taxrate=tax.getRate();
            double gsttotal=(total * taxrate)/100;
            double totalamount=gsttotal + total;
            cartItem.setTotalamount((float) totalamount);
            cartItem.setOrders(shoppingCart);
            cartItemList.add(cartItem);

        } else {

            cartItem = new OrderDetail();
            cartItem.setProducts(product);
            cartItem.setOrders(shoppingCart);
            cartItem.setQuantity(Quntity);

            float total = (float) (Quntity * product.getProductPrice());
            cartItem.setUnitPrice((float) total);
            Tax tax=product.getCategories().getTaxes();
            double taxrate=tax.getRate();
            double gsttotal=(total * taxrate)/100;
            double totalamount=gsttotal + total;
            cartItem.setTotalamount((float) totalamount);
            cartItem.setOrders(shoppingCart);

            cartItemList.add(cartItem);

            int remainqty = product.getProductQuantity() - Quntity;
            product.setProductQuantity(remainqty);
            product.setProductID(product.getProductID());
            try {
                productRepo.save(product);
            }catch (Exception e)
            {
                e.getCause();
            }
            ProductTransaction pt = new ProductTransaction();
            pt.setQuantity(Quntity);
            pt.setAdded(false);
            pt.setProducts(product);
            pt.setCustomerID(customer);
            pt.setSuppliers(product.supplier);

            try {
                ptr.save(pt);
            }catch (Exception e)
            {
                e.getCause();
            }
            try {
                itemRepository.save(cartItem);
            } catch (Exception ex) {
                ex.getCause();
            }


        }
        if (shoppingCart.getOrderDetails() == null) {


            shoppingCart.setOrderDetails(cartItemList);
            double totalPrice = totalprice(shoppingCart.getOrderDetails());
            int remainqty = product.getProductQuantity() - Quntity;
            product.setProductQuantity(remainqty);
            product.setProductID(product.getProductID());
            try {
                productRepo.save(product);
            }catch (Exception e)
            {
                e.getCause();
            }

            ProductTransaction pt = new ProductTransaction();
            pt.setQuantity(Quntity);
            pt.setAdded(false);
            pt.setProducts(product);
            pt.setCustomerID(customer);
            pt.setSuppliers(product.supplier);

            try {
                ptr.save(pt);
            }catch (Exception e)
            {
                e.getCause();
            }
            shoppingCart.setTotalPrice((float) totalPrice);
           // shoppingCart.setTotalTax((float) finaltax);
            shoppingCart.setCustomers(customer);
            cartItem.setOrders(shoppingCart);
            try {
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                Date date = new Date();
                shoppingCart.setOrderDate(dateFormat.format(date));
                cartRepository.save(shoppingCart);
                itemRepository.save(cartItem);
            } catch (Exception e) {
                e.getCause();
            }

        } else {
            shoppingCart.setOrderDetails(cartItemList);
            double totalPrice = totalprice(shoppingCart.getOrderDetails());
            shoppingCart.setTotalPrice((float) totalPrice);
//            double test=totalunitprice(shoppingCart.getOrderDetails());
//            double finaltax=totalPrice-test;
           // double finaltax=totaltaxcalc(shoppingCart.getOrderDetails());
           // shoppingCart.setTotalTax((float) finaltax);
            int remainqty = product.getProductQuantity() - Quntity;
            product.setProductQuantity(remainqty);
            product.setProductID(product.getProductID());
            try {
                productRepo.save(product);
            }catch (Exception e)
            {
                e.getCause();
            }

            shoppingCart.setCustomers(customer);
            shoppingCart.setOrderID(shoppingCart.getOrderID());
            cartItem.setOrders(shoppingCart);
            try {

                cartRepository.save(shoppingCart);
            }catch (Exception e)
            {
                e.getCause();
            }
        }
        return shoppingCart;
    }



}
