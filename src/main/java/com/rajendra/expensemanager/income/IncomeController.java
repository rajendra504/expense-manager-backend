package com.rajendra.expensemanager.income;

import com.rajendra.expensemanager.exception.ApiResponse;
import com.rajendra.expensemanager.income.dto.FinancialSummaryResponse;
import com.rajendra.expensemanager.income.dto.IncomeRequest;
import com.rajendra.expensemanager.income.dto.IncomeResponse;
import com.rajendra.expensemanager.income.dto.MonthlySummaryResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("api/v1/income")
public class IncomeController {
    private final IncomeService incomeService;

    public IncomeController(IncomeService incomeService) {
        this.incomeService = incomeService;
    }
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<IncomeResponse>> createIncome(
            @Valid @RequestBody IncomeRequest request, HttpServletRequest httpRequest){
        IncomeResponse response = incomeService.createIncome(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true,
                        "Income Created Successfully",
                        response,
                        null,
                        httpRequest.getRequestURI()
                ));
    }
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<IncomeResponse>> updateIncome(
           @PathVariable Long id, @Valid @RequestBody IncomeRequest request, HttpServletRequest httpRequest){
        IncomeResponse response = incomeService.updateIncome(id,request);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(true,
                        "Income Updated Successfully",
                        response,
                        null,
                        httpRequest.getRequestURI()
                ));
    }
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<IncomeResponse>> getIncomeById(
            @PathVariable Long id, HttpServletRequest httpRequest){
        IncomeResponse response = incomeService.getIncomeById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(true,
                        "Income Fetched Successfully",
                        response,
                        null,
                        httpRequest.getRequestURI()
                ));
    }
    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<Page<IncomeResponse>>> getAllIncomes(
            Pageable pageable,
            HttpServletRequest httpRequest) {
        Page<IncomeResponse> incomes =
                incomeService.getAllIncomes(pageable);
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Income list fetched successfully",
                        incomes,
                        null,
                        httpRequest.getRequestURI()
                )
        );
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable Long id,
            HttpServletRequest httpRequest) {
        incomeService.deleteIncome(id);
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Income deleted successfully",
                        null,
                        null,
                        httpRequest.getRequestURI()
                )
        );
    }

    @GetMapping("/summary")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<FinancialSummaryResponse>> summary(
            HttpServletRequest request) {
        FinancialSummaryResponse summary =
                incomeService.getFinancialSummary();
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Financial summary fetched",
                        summary,
                        null,
                        request.getRequestURI()
                )
        );
    }

    @GetMapping("/monthly-summary")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<List<MonthlySummaryResponse>>> monthlySummary(
            @RequestParam(defaultValue = "6") int months,
            HttpServletRequest request) {

        List<MonthlySummaryResponse> data = incomeService.getMonthlySummary(months);
        return ResponseEntity.ok(new ApiResponse<>(
                true,
                "Monthly summary fetched",
                data,
                null,
                request.getRequestURI()
        ));
    }
}
