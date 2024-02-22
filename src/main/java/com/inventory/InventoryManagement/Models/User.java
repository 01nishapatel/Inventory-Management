package com.inventory.InventoryManagement.Models;

import com.inventory.InventoryManagement.annotation.FieldsValueMatch;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Setter
@Entity
@Table(name = "user")
@FieldsValueMatch(
        field = "Password",
        fieldMatch = "ConfirmPassword",
        message = "Passwords do not match !!"
)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "UserID")
    public int UserID;

    @NotBlank(message = "UserName must not be blank")
    @Column(name = "UserName")
    private String UserName;

    @NotBlank(message = "Email must not be blank")
    @Email(message = "Please provide a valid email")
    @Column(name = "CustomerEmail")
    private String email;

    @NotBlank(message = "Password must not be blank")
    @Column(name = "Password")
    // @Size(min = 8, message = "Password must be at least 8 characters long")
    private String Password;

    @NotBlank(message = "Confirm Password must not be blank")
    @Column(name = "ConfirmPassword")
    private String confirmPassword;

    @Column(name = "IsActive")
    private Boolean IsActive;

    @Column(name = "ParentUserID")
    private int parentUserID;

    @Column(name = "ResetPasswordToken")
    private String resetPasswordToken;

//    @ManyToOne(fetch=FetchType.LAZY,optional = false)
//    @JoinColumn(name = "RoleID", referencedColumnName = "RoleID")
//    private Role RoleID;

    @JoinColumn(name = "RoleID", referencedColumnName = "RoleID")
    @ManyToOne(optional = false)
    public Role roleID;

    @OneToOne(mappedBy = "users")
    public CompanyDetails companies;

    //    @ManyToOne(fetch=FetchType.LAZY,optional = false)
//    @JoinColumn(name = "SubscriptionID", referencedColumnName = "SubscriptionID")
//    private Subscription SubscriptionID;
    @JoinColumn(name = "SubscriptionID", referencedColumnName = "SubscriptionID")
    @ManyToOne(optional = false)
    public Subscription subscriptions;
}
