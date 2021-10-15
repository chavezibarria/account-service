package com.jgci.springboot.app.account.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import static com.jgci.springboot.app.account.constants.Constants.MIN_BALANCE_VALUE;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table(name = "accounts")
public class Account implements Serializable {

    @Id
    @NotNull
    @NotBlank
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Min(MIN_BALANCE_VALUE)
    @NotNull
    private BigDecimal balance;

    @Column(name = "transfers_today")
    @NotNull
    private int transfersToday;

    @ElementCollection
    @Column(name = "errors")
    private List<String> errors;

    private static final long serialVersionUID = 6924139093794076474L;


    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public void setTransfersToday(int transfersToday) {
        this.transfersToday = transfersToday;
    }
}
