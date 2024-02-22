package com.inventory.InventoryManagement.Models;

import groovy.transform.Trait;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "productMaster")

public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "ProductID")
    public int ProductID;

    @NotBlank(message = "Product Name must not be blank")
    @Column(name = "ProductName")
    private String ProductName;

    @Column(name = "ProductDescription")
    private String ProductDescription;

//    @Positive
//    @Min(1)
    @NotNull(message = "Product Price must not be blank")
    @DecimalMin(value = "0.0",inclusive = false)
    @Column(name = "ProductPrice")
    public double ProductPrice;

//    @Positive
//    @Min(1)
    @NotNull(message = "Product Quantity must not be blank")
    @DecimalMin(value = "0",inclusive = false)
    @Column(name = "ProductQuantity")
    public int productQuantity;

//    @ManyToOne(fetch=FetchType.LAZY,optional = false)
//    @JoinColumn(name = "CategoryID", referencedColumnName = "CategoryID")
//    private Category CategoryID;

    @JoinColumn(name = "CategoryID", referencedColumnName = "CategoryID")
    @ManyToOne(optional = false)
    public Category categories;

    @JoinColumn(name = "SupplierID", referencedColumnName = "SupplierID")
    @ManyToOne(optional = false)
    public Supplier supplier;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "products")
    private List<ProductTransaction> productTransactions;

//    @OneToMany(mappedBy = "OrderDetailID", fetch = FetchType.LAZY,
//            cascade = CascadeType.PERSIST, targetEntity = OrderDetail.class)
//    private List<OrderDetail> orderDetails;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "products")
    private List<OrderDetail> orderDetails;

    public int getProductID() {
        return ProductID;
    }

    public void setProductID(int productID) {
        ProductID = productID;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getProductDescription() {
        return ProductDescription;
    }

    public void setProductDescription(String productDescription) {
        ProductDescription = productDescription;
    }

    public double getProductPrice() {
        return ProductPrice;
    }

    public void setProductPrice(double productPrice) {
        ProductPrice = productPrice;
    }



    public Category getCategories() {
        return categories;
    }

    public void setCategories(Category categories) {
        this.categories = categories;
    }

    public List<ProductTransaction> getProductTransactions() {
        return productTransactions;
    }

    public void setProductTransactions(List<ProductTransaction> productTransactions) {
        this.productTransactions = productTransactions;
    }

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }
}
