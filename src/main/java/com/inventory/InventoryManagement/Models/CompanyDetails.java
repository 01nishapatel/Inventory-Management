package com.inventory.InventoryManagement.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Getter
@Setter
@Entity
@Table(name="CompanyDetails")
public class CompanyDetails extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "companyID")
    public int companyID;

    @NotBlank(message = "Company Name must not be blank")
    @Column(name = "companyName")
    private String companyName;

    @NotBlank(message = "Email must not be blank")
    @Email(message = "Please provide a valid email")
    @Column(name = "companyEmail")
    private String companyEmail;

    @NotBlank(message = "phone no must not be blank")
    @Pattern(regexp = "(^$|[0-9]{10})",message = "Mobile number must be 10 digits")
    @Column(name = "phoneNo")
    private String phoneNo;

    @NotBlank(message = "Company Address must not be blank")
    @Column(name = "companyAddress")
    private String companyAddress;

    @Column(name = "logoName")
    private String logoName;

    @Column(name = "logoType")
    private String logoType;

    @Lob
    @Column(name = "companyLogo")
    private byte[] companyLogo;

    @JoinColumn(name = "UserID", referencedColumnName = "UserID")
    @OneToOne(optional = false,targetEntity = User.class)
    public User users;

    @Transient
    private MultipartFile logoFile;

    public void setLogoFile(MultipartFile logoFile) throws IOException {
        this.logoFile = logoFile;
        this.companyLogo = logoFile.getBytes();
        this.logoName = logoFile.getOriginalFilename();
        this.logoType = logoFile.getContentType();
    }
//    public int getCompanyID() {
//        return companyID;
//    }
//
//    public void setCompanyID(int companyID) {
//        this.companyID = companyID;
//    }
//
//    public String getCompanyName() {
//        return companyName;
//    }
//
//    public void setCompanyName(String companyName) {
//        this.companyName = companyName;
//    }
//
//    public String getCompanyEmail() {
//        return companyEmail;
//    }
//
//    public void setCompanyEmail(String companyEmail) {
//        this.companyEmail = companyEmail;
//    }
//
//    public byte[] getCompanyLogo() {
//        return companyLogo;
//    }
//
//    public void setCompanyLogo(byte[] companyLogo) {
//        this.companyLogo = companyLogo;
//    }
//
//    public String getLogoName() {
//        return logoName;
//    }
//
//    public void setLogoName(String logoName) {
//        this.logoName = logoName;
//    }
//
//    public String getLogoType() {
//        return logoType;
//    }
//
//    public void setLogoType(String logoType) {
//        this.logoType = logoType;
//    }
//
//    public String getPhoneNo() {
//        return phoneNo;
//    }
//
//    public void setPhoneNo(String phoneNo) {
//        this.phoneNo = phoneNo;
//    }
//
//    public String getCompanyAddress() {
//        return companyAddress;
//    }
//
//    public void setCompanyAddress(String companyAddress) {
//        this.companyAddress = companyAddress;
//    }
//
//    public User getUsers() {
//        return users;
//    }
//
//    public void setUsers(User users) {
//        this.users = users;
//    }
}
