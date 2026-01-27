package com.business.domain;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;

import java.util.Objects;

@Entity(name = "CUSTOMER_ACCOUNT")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Customer_X_Account {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customer_account_seq_gen")
    @SequenceGenerator(name = "customer_account_seq_gen", sequenceName = "customer_account_seq", allocationSize = 1)
    @Column(name = "CUSTOMER_X_ACCOUNT_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CUSTOMER_ID", nullable = false)
    private Customer customerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ACCOUNT_ID", nullable = false)
    private Account accountId;
    @Column(name = "ROLE")
    private String role;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Customer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Customer customerId) {
        this.customerId = customerId;
    }

    public Account getAccountId() {
        return accountId;
    }

    public void setAccountId(Account accountId) {
        this.accountId = accountId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "Customer_X_Account{" +
                " id =" + id +
                ", role='" + role + '\'' +
                '}';
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Customer_X_Account that)) return false;

        return Objects.equals(getId(), that.getId()) && Objects.equals(getRole(), that.getRole());
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(getId());
        result = 31 * result + Objects.hashCode(getRole());
        return result;
    }
}
