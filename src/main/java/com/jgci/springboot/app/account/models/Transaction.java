package com.jgci.springboot.app.account.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Transaction implements Serializable {

    private static final long serialVersionUID = 1307696094720721308L;

    @NotNull(message = "Account Id field is mandatory.")
    @NotBlank(message = "Account Id field is mandatory.")
    @JsonProperty("id")
    private String accountId;

    @NotNull(message = "deposit field is mandatory.")
    @JsonProperty("deposit")
    private boolean isDeposit;

    @JsonProperty("amount")
    @NotNull(message = "Amount field is mandatory")
    @DecimalMin(".000001")
    private BigDecimal amount;

}
