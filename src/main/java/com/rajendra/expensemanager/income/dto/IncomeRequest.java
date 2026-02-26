package com.rajendra.expensemanager.income.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
@Getter
@Setter
public class IncomeRequest {

    @NotBlank
    private String source;//salary ,freelance,gift,rent etc...

    @NotNull
    private BigDecimal amount;

    @NotNull
    private LocalDate date;

    @Size(max = 500)
    private String description;

}
