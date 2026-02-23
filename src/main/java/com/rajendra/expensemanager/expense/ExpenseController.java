package com.rajendra.expensemanager.expense;

import com.rajendra.expensemanager.exception.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/expenses")
public class ExpenseController {

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> userAccess() {

        return ResponseEntity.ok(
                new ApiResponse<>(true, "User content", "Access granted for USER")
        );
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> adminAccess() {

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Admin content", "Access granted for ADMIN")
        );
    }
}