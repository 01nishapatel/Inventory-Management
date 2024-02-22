package com.inventory.InventoryManagement.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Setter
@Entity
@Table(name = "orderDetailMaster")
public class OrderDetail extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,generator = "native")
    @GenericGenerator(name = "native",strategy = "native")
    @Column(name = "OrderDetailID")
    private int OrderDetailID;

    @Column(name = "Quantity")
    private int Quantity;

    @Column(name="UnitPrice")
    public float UnitPrice;

    @Column(name="TotalAmount")
    public float Totalamount;

    @ManyToOne(fetch=FetchType.LAZY ,optional = false)
    @JoinColumn(name = "OrderID", referencedColumnName = "OrderID")
    private Order orders;


    //    @ManyToOne(fetch=FetchType.LAZY,optional = false)
//    @JoinColumn(name = "ProductID", referencedColumnName = "ProductID")
//    private Product ProductID;
    @JoinColumn(name = "ProductID", referencedColumnName = "ProductID")
    @ManyToOne(optional = false)
    public Product products;


}
