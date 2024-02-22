package com.inventory.InventoryManagement.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name="subscription")
public class Subscription extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,generator = "native")
    @GenericGenerator(name = "native",strategy = "native")
    @Column(name="SubscriptionID")
    public int subscriptionID;

    @NotBlank(message = "Subscription name must not be blank")
    @Column(name="SubscriptionName")
    private String subscriptionName;

    @DecimalMin(value = "0.0",inclusive = false)
    @Column(name="SubscriptionPrice")
    private float SubscriptionPrice;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "subscriptions")
    private List<User> users;
}
