package com.rajendra.expensemanager.expense;

import com.rajendra.expensemanager.exception.ExpenseNotFoundException;
import com.rajendra.expensemanager.exception.UserNotFoundException;
import com.rajendra.expensemanager.expense.dto.ExpenseRequest;
import com.rajendra.expensemanager.expense.dto.ExpenseResponse;
import com.rajendra.expensemanager.user.User;
import com.rajendra.expensemanager.user.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ExpenseService {
    private final UserRepository userRepository;
    private final ExpenseRepository expenseRepository;

    public ExpenseService(UserRepository userRepository, ExpenseRepository expenseRepository) {
        this.userRepository = userRepository;
        this.expenseRepository = expenseRepository;
    }

    public ExpenseResponse createExpense(ExpenseRequest request){
        User user = getCurrentUser();

        Expense expense = new Expense();
        expense.setTitle(request.getTitle());
        expense.setDescription(request.getDescription());
        expense.setAmount(request.getAmount());
        expense.setCategory(request.getCategory());
        expense.setDate(request.getDate());
        expense.setUser(user);

        Expense saved = expenseRepository.save(expense);
        return mapToResponse(saved);
    }
    public ExpenseResponse updateExpense(Long id,ExpenseRequest request){
        User user = getCurrentUser();

        Expense expense = expenseRepository.findByIdAndUser(id,user)
                .orElseThrow(() -> new ExpenseNotFoundException("Expense not found"));
        expense.setTitle(request.getTitle());
        expense.setDescription(request.getDescription());
        expense.setAmount(request.getAmount());
        expense.setCategory(request.getCategory());
        expense.setDate(request.getDate());

        Expense updated = expenseRepository.save(expense);
        return mapToResponse(updated);
    }
    public ExpenseResponse getExpenseById(Long id){
        User user = getCurrentUser();

        Expense expense = expenseRepository.findByIdAndUser(id,user)
                .orElseThrow(() -> new ExpenseNotFoundException("Expense not found"));
        return mapToResponse(expense);
    }
    public Page<ExpenseResponse> getAllExpenses(
            int page,
            int size,
            String category,
            BigDecimal minAmount,
            BigDecimal maxAmount,
            String search
    ){
        User user = getCurrentUser();
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());

        Specification<Expense> spec =
                ExpenseSpecification.filterExpenses(user, category, minAmount, maxAmount, search);

        Page<Expense> expenses = expenseRepository.findAll(spec, pageable);
       return expenses.map(this::mapToResponse);

    }
    public List<String> getUserCategories() {
        User user = getCurrentUser();
        return expenseRepository.findDistinctCategoriesByUser(user);
    }
    public void deleteExpense(Long id){
        User user = getCurrentUser();

        Expense expense = expenseRepository.findByIdAndUser(id,user)
                .orElseThrow(() -> new ExpenseNotFoundException("Expense not found"));

       expenseRepository.delete(expense);
    }
    //Helper methods
    private User getCurrentUser(){
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User Not Found"));
    }
    private ExpenseResponse mapToResponse(Expense expense){
        ExpenseResponse response = new ExpenseResponse();
        response.setId(expense.getId());
        response.setTitle(expense.getTitle());
        response.setDescription(expense.getDescription());
        response.setAmount(expense.getAmount());
        response.setCategory(expense.getCategory());
        response.setDate(expense.getDate());
        response.setUserId(expense.getUser().getId());

        return response;
    }
}
