package com.business.domain;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.sql.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity(name = "CUSTOMER")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customer_seq_gen")
    @SequenceGenerator(name = "customer_seq_gen", sequenceName = "customer_seq", allocationSize = 1)
    @Column(name = "CUSTOMER_ID")
    private Long customerId;
    @NotBlank(message = "First Name is mandatory")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    @Column(name = "FNAME")
    private String firstName;
    @Column(name = "MNAME")
    private String middleName;
    @Column(name = "LNAME")
    private String lastName;
    @NotNull(message = "Date of Birth is mandatory")
    @Past
    @Column(name = "DOB")
    private Date dob;
    @NotBlank(message = "Mobile Number is mandatory")
    @Size(min = 10, max = 10, message = "Mobile Number should be exactly 10 digit long")
    @Column(name = "CONTACTNO")
    private String mobile;
    @Column(name = "STATUS")
    private String status = "A";
    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "CUSTOMER_ID", referencedColumnName = "CUSTOMER_ID")
    private Set<Customer_X_Account> customerAccounts = new HashSet<>();

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Set<Customer_X_Account> getCustomerAccounts() {
        return customerAccounts;
    }

    public void setCustomerAccounts(Set<Customer_X_Account> customerAccounts) {
        this.customerAccounts = customerAccounts;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "customerId=" + customerId +
                ", firstName='" + firstName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", dob=" + dob +
                ", mobile='" + mobile + '\'' +
                ", status='" + status + '\'' +
                ", customerAccounts=" + customerAccounts +
                '}';
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Customer customer)) return false;

        return getCustomerId().equals(customer.getCustomerId()) && getFirstName().equals(customer.getFirstName()) && Objects.equals(getMiddleName(), customer.getMiddleName()) && Objects.equals(getLastName(), customer.getLastName()) && getDob().equals(customer.getDob()) && getMobile().equals(customer.getMobile()) && Objects.equals(getStatus(), customer.getStatus());
    }

    @Override
    public int hashCode() {
        int result = getCustomerId().hashCode();
        result = 31 * result + getFirstName().hashCode();
        result = 31 * result + Objects.hashCode(getMiddleName());
        result = 31 * result + Objects.hashCode(getLastName());
        result = 31 * result + getDob().hashCode();
        result = 31 * result + getMobile().hashCode();
        result = 31 * result + Objects.hashCode(getStatus());
        return result;
    }
}
