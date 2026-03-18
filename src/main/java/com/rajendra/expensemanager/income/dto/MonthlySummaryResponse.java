package com.rajendra.expensemanager.income.dto;

public record MonthlySummaryResponse(
        int year,
        int month,
        java.math.BigDecimal totalIncome,
        java.math.BigDecimal totalExpense
) {}