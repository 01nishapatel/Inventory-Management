package com.inventory.InventoryManagement.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
@Getter
@Setter
@Entity
@Table(name = "Tax")
public class Tax {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,generator = "native")
    @GenericGenerator(name = "native",strategy = "native")
    @Column(name = "TaxID")
    public int TaxID;

    @NotBlank(message = "Tax Name must not be blank")
    @Column(name = "TaxName")
    private String TaxName;

    @DecimalMin(value = "0.0",inclusive = false)
    @Column(name = "Rate")
    private double Rate;

    @JoinColumn(name = "CategoryID", referencedColumnName = "CategoryID")
    @ManyToOne(optional = false)
    public Category categories;
}
