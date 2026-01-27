package com.business.domain;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity(name = "ACCOUNT")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_seq_gen")
    @SequenceGenerator(name = "account_seq_gen", sequenceName = "account_seq", allocationSize = 1)
    @Column(name = "ACCOUNT_ID")
    private Long accountId;
    @Column(name = "ACCOUNTNUMBER")
    private String accountNumber;
    @Column(name = "ACCOUNT_TYPE")
    private String accountType;
    @Column(name = "CURRENCY")
    private String currency;
    @Column(name = "BRANCH_ID")
    private Long branchID;
    @Column(name = "STATUS")
    private String status="A";
    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "ACCOUNT_ID", referencedColumnName = "ACCOUNT_ID")
    private Set<Customer_X_Account> customerAccounts = new HashSet<>();

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Long getBranchID() {
        return branchID;
    }

    public void setBranchID(Long branchID) {
        this.branchID = branchID;
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
        return "Account{" +
                "accountId=" + accountId +
                ", accountNumber='" + accountNumber + '\'' +
                ", accountType='" + accountType + '\'' +
                ", currency='" + currency + '\'' +
                ", branchID=" + branchID +
                ", status='" + status +
                '}';
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Account account)) return false;

        return getAccountId().equals(account.getAccountId()) && getAccountNumber().equals(account.getAccountNumber()) && getAccountType().equals(account.getAccountType()) && Objects.equals(getCurrency(), account.getCurrency()) && Objects.equals(getBranchID(), account.getBranchID()) && Objects.equals(getStatus(), account.getStatus());
    }

    @Override
    public int hashCode() {
        int result = getAccountId().hashCode();
        result = 31 * result + getAccountNumber().hashCode();
        result = 31 * result + getAccountType().hashCode();
        result = 31 * result + Objects.hashCode(getCurrency());
        result = 31 * result + Objects.hashCode(getBranchID());
        result = 31 * result + Objects.hashCode(getStatus());
        return result;
    }
}
