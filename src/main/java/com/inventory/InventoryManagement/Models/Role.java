package com.inventory.InventoryManagement.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "role")
public class Role extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "RoleID")
    public int roleID;

    @NotBlank(message = "Role Name must not be blank")
    @Column(name = "RoleName")
    public String roleName;

    @Column(name = "RoleDescription")
    public String roleDescription;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "roleID")
    private List<User> users;
}
