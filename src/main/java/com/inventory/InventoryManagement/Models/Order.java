package com.inventory.InventoryManagement.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "orderMaster")
    public class Order extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,generator = "native")
    @GenericGenerator(name = "native",strategy = "native")
    @Column(name = "OrderID")
    private int OrderID;

    @Column(name = "OrderDate")
    public String OrderDate;

    @Column(name = "TotalPrice")
    public float TotalPrice;

    @Column(name = "PaymentType")
    private String PaymentType;

    @ManyToOne(fetch=FetchType.LAZY,optional = false)
    @JoinColumn(name = "CustomerID", referencedColumnName = "CustomerID")
    private Customer customers;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "orders")
    private List<OrderDetail> orderDetails;

    @Column(name = "TotalTax")
    public float TotalTax;

    public enum PaymentType{
        ONLINE,OFFLINE
    }

}