package com.inventory.InventoryManagement.Repositary;

import com.inventory.InventoryManagement.Models.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionRepo extends JpaRepository<Subscription,Integer> {
    Subscription findSubscriptionNameBySubscriptionID(int subscriptionID);

    Subscription findBySubscriptionName(String subscription);
}
