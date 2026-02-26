package com.rajendra.expensemanager.income.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class IncomeResponse {
    private Long id;
    private String source;
    private BigDecimal amount;
    private LocalDate date;
    private String description;
    private Long userId;
}
