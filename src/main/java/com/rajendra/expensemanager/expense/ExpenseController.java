package com.rajendra.expensemanager.expense;

import com.rajendra.expensemanager.exception.ApiResponse;
import com.rajendra.expensemanager.expense.dto.ExpenseRequest;
import com.rajendra.expensemanager.expense.dto.ExpenseResponse;
import jakarta.validation.Valid;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("api/v1/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }
    @PostMapping("/bulk")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<List<ExpenseResponse>>> createExpenses(
            @Valid @RequestBody List<ExpenseRequest> requests,
            HttpServletRequest httpRequest) {

        List<ExpenseResponse> expenses = requests.stream()
                .map(expenseService::createExpense)
                .toList();

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ApiResponse<>(
                        true,
                        "Expenses created successfully",
                        expenses,
                        null,
                        httpRequest.getRequestURI()
                )
        );
    }
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<ExpenseResponse>> createExpense(
            @Valid @RequestBody ExpenseRequest request,
            HttpServletRequest httpRequest) {

        ExpenseResponse expense = expenseService.createExpense(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ApiResponse<>(
                        true,
                        "Expense created successfully",
                        expense,
                        null,
                        httpRequest.getRequestURI()
                )
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<ExpenseResponse>> getExpenseById(
            @PathVariable Long id,
            HttpServletRequest httpRequest) {

        ExpenseResponse expense = expenseService.getExpenseById(id);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Expense fetched successfully",
                        expense,
                        null,
                        httpRequest.getRequestURI()
                )
        );
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<Page<ExpenseResponse>>> getAllExpenses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) BigDecimal minAmount,
            @RequestParam(required = false) BigDecimal maxAmount,
            @RequestParam(required = false) String search,
            HttpServletRequest httpRequest) {
        Page<ExpenseResponse> response =
                expenseService.getAllExpenses(page, size, category, minAmount, maxAmount, search);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Expenses fetched successfully",
                        response,
                        null,
                        httpRequest.getRequestURI()
                )
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<ExpenseResponse>> updateExpense(
            @PathVariable Long id,
            @Valid @RequestBody ExpenseRequest request,
            HttpServletRequest httpRequest) {

        ExpenseResponse expense = expenseService.updateExpense(id, request);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Expense updated successfully",
                        expense,
                        null,
                        httpRequest.getRequestURI()
                )
        );
    }
    @GetMapping("/categories")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<List<String>>> getCategories(HttpServletRequest httpRequest) {
        List<String> categories = expenseService.getUserCategories();

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Categories fetched successfully",
                        categories,
                        null,
                        httpRequest.getRequestURI()
                )
        );
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<Object>> deleteExpense(
            @PathVariable Long id,
            HttpServletRequest httpRequest) {

        expenseService.deleteExpense(id);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Expense deleted successfully",
                        null,
                        null,
                        httpRequest.getRequestURI()
                )
        );
    }
}