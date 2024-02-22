package com.inventory.InventoryManagement.Models;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "categoryMaster")
public class Category extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,generator = "native")
    @GenericGenerator(name = "native",strategy = "native")
    @Column(name = "CategoryID")
    public int CategoryID;

    @NotBlank(message = "CategoryName must not be blank")
    @Column(name = "CategoryName")
    private String CategoryName;

    @Column(name = "CategoryDescription")
    private String CategoryDescription;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "categories")
    private List<Product> products;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "categories")
    private Tax taxes;

    public int getCategoryID() {
        return CategoryID;
    }

    public void setCategoryID(int categoryID) {
        CategoryID = categoryID;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }

    public String getCategoryDescription() {
        return CategoryDescription;
    }

    public void setCategoryDescription(String categoryDescription) {
        CategoryDescription = categoryDescription;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public Tax getTaxes() {
        return taxes;
    }

    public void setTaxes(Tax taxes) {
        this.taxes = taxes;
    }
}
