package com.inventory.InventoryManagement.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "customerMaster")
public class Customer extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,generator = "native")
    @GenericGenerator(name = "native",strategy = "native")
    @Column(name = "CustomerID")
    public int CustomerID;

    @Column(name = "CustomerName")
    @NotBlank(message = "CustomerName must not be blank")
    private String CustomerName;

    @NotBlank(message = "Email must not be blank")
    @Email(message = "Please provide a valid email")
    @Column(name = "CustomerEmail")
    private String email;

    @NotBlank(message = "Customer Mobile Number must not be blank")
    @Column(name = "CustomerPhone")
    @Pattern(regexp = "(^$|[0-9]{10})",message = "Mobile number must be 10 digits")
    private String phonenumber;

    @Column(name = "CustomerAddress")
    private String CustomerAddress;

//    @OneToMany(mappedBy = "OrderID", fetch = FetchType.LAZY,
//            cascade = CascadeType.PERSIST, targetEntity = Order.class)
//    private List<Order> orders;
@OneToMany(cascade = CascadeType.ALL, mappedBy = "customers")
private List<Order> orders;

    @OneToMany(mappedBy = "ProductTransactionID", fetch = FetchType.LAZY,
            cascade = CascadeType.PERSIST, targetEntity = ProductTransaction.class)
    private List<ProductTransaction> productTransactions;

    public int getCustomerID() {
        return CustomerID;
    }

    public void setCustomerID(int customerID) {
        CustomerID = customerID;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getCustomerAddress() {
        return CustomerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        CustomerAddress = customerAddress;
    }



    public List<ProductTransaction> getProductTransactions() {
        return productTransactions;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public void setProductTransactions(List<ProductTransaction> productTransactions) {
        this.productTransactions = productTransactions;
    }
}
