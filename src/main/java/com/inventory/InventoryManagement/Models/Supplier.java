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
@Table(name = "supplier")
public class Supplier extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "SupplierID")
    public int SupplierID;

    @Column(name = "SupplierName")
    @NotBlank(message = "SupplierName must not be blank")
    private String SupplierName;

    @NotBlank(message = "Email must not be blank")
    @Email(message = "Please provide a valid email")
    @Column(name = "SupplierEmail")
    private String supplierEmail;

    @NotBlank(message = "Supplier Mobile Number must not be blank")
    @Column(name = "SupplierPhone")
    @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits")
    private String SupplierPhone;

    @Column(name = "SupplierAddress")
    private String SupplierAddress;

    //    @OneToMany(mappedBy = "ProductTransactionID",
    //            cascade = CascadeType.PERSIST, targetEntity = ProductTransaction.class)
    //    private List<ProductTransaction> productTransactions;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "suppliers")
    private List<ProductTransaction> productTransactions;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "supplier",targetEntity = Product.class)
    private List<Product> products;

    public int getSupplierID() {
        return SupplierID;
    }

    public void setSupplierID(int supplierID) {
        SupplierID = supplierID;
    }

    public String getSupplierName() {
        return SupplierName;
    }

    public void setSupplierName(String supplierName) {
        SupplierName = supplierName;
    }

    public String getSupplierEmail() {
        return supplierEmail;
    }

    public void setSupplierEmail(String supplierEmail) {
        this.supplierEmail = supplierEmail;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public String getSupplierPhone() {
        return SupplierPhone;
    }

    public void setSupplierPhone(String supplierPhone) {
        SupplierPhone = supplierPhone;
    }

    public String getSupplierAddress() {
        return SupplierAddress;
    }

    public void setSupplierAddress(String supplierAddress) {
        SupplierAddress = supplierAddress;
    }

    public List<ProductTransaction> getProductTransactions() {
        return productTransactions;
    }

    public void setProductTransactions(List<ProductTransaction> productTransactions) {
        this.productTransactions = productTransactions;
    }
}
