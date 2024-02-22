package com.inventory.InventoryManagement.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Setter
@Entity
@Table(name = "producttransaction")
public class ProductTransaction extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,generator = "native")
    @GenericGenerator(name = "native",strategy = "native")
    @Column(name = "ProductTransactionID")
    private int ProductTransactionID;

    @Column(name = "Quantity")
    @NotNull(message = "Quantity must not be blank")
    private int Quantity;

    @Column(name = "IsAdded")
    private boolean IsAdded;

//    @ManyToOne(optional = false)
//    @JoinColumn(name = "ProductID", referencedColumnName = "ProductID")
//    private Product ProductID;

    @JoinColumn(name = "ProductID", referencedColumnName = "ProductID")
    @ManyToOne(optional = false)
    public Product products;

    @ManyToOne(optional = false)
    @JoinColumn(name = "CustomerID", referencedColumnName = "CustomerID")
    private Customer CustomerID;

//    @ManyToOne(fetch=FetchType.LAZY,optional = false)
//    @JoinColumn(name = "SupplierID", referencedColumnName = "SupplierID")
//    private Supplier SupplierID;

    @JoinColumn(name = "SupplierID", referencedColumnName = "SupplierID")
    @ManyToOne(optional = false)
    public Supplier suppliers;

    public int getProductTransactionID() {
        return ProductTransactionID;
    }

    public void setProductTransactionID(int productTransactionID) {
        ProductTransactionID = productTransactionID;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public boolean isAdded() {
        return IsAdded;
    }

    public void setAdded(boolean added) {
        IsAdded = added;
    }

    public Product getProducts() {
        return products;
    }

    public void setProducts(Product products) {
        this.products = products;
    }

    public Customer getCustomerID() {
        return CustomerID;
    }

    public void setCustomerID(Customer customerID) {
        CustomerID = customerID;
    }

    public Supplier getSuppliers() {
        return suppliers;
    }

    public void setSuppliers(Supplier suppliers) {
        this.suppliers = suppliers;
    }
}
