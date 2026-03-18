package com.rajendra.expensemanager.income;

import com.rajendra.expensemanager.exception.IncomeNotFoundException;
import com.rajendra.expensemanager.exception.UserNotFoundException;
import com.rajendra.expensemanager.expense.ExpenseRepository;
import com.rajendra.expensemanager.income.dto.FinancialSummaryResponse;
import com.rajendra.expensemanager.income.dto.IncomeRequest;
import com.rajendra.expensemanager.income.dto.IncomeResponse;
import com.rajendra.expensemanager.income.dto.MonthlySummaryResponse;
import com.rajendra.expensemanager.user.User;
import com.rajendra.expensemanager.user.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@Service
public class IncomeService {
    private final UserRepository userRepository;
    private final IncomeRepository incomeRepository;
    private final ExpenseRepository expenseRepository;

    public IncomeService(UserRepository userRepository, IncomeRepository incomeRepository, ExpenseRepository expenseRepository) {
        this.userRepository = userRepository;
        this.incomeRepository = incomeRepository;
        this.expenseRepository = expenseRepository;
    }

    public IncomeResponse createIncome(IncomeRequest request){
        User user = getCurrentUser();

        Income income = new Income();
        income.setSource(request.getSource());
        income.setAmount(request.getAmount());
        income.setDescription(request.getDescription());
        income.setDate(request.getDate());
        income.setUser(user);

        return mapToResponse(incomeRepository.save(income));
    }
    public IncomeResponse updateIncome(Long id,IncomeRequest request){
        User user = getCurrentUser();
        Income income = incomeRepository.findByIdAndUser(id,user)
                .orElseThrow(()-> new IncomeNotFoundException("Income Not Found"));

        income.setSource(request.getSource());
        income.setAmount(request.getAmount());
        income.setDescription(request.getDescription());
        income.setDate(request.getDate());
        income.setUser(user);

        return mapToResponse(incomeRepository.save(income));
    }
    public Page<IncomeResponse> getAllIncomes(Pageable pageable){
        User user = getCurrentUser();

        return incomeRepository.findByUser(user,pageable)
                .map(this::mapToResponse);
    }
    public IncomeResponse getIncomeById(Long id){
        User user = getCurrentUser();
        Income income = incomeRepository.findByIdAndUser(id,user)
                .orElseThrow(()-> new IncomeNotFoundException("Income Not Found"));

        return mapToResponse(income);
    }
    public void deleteIncome(Long id){
        User user = getCurrentUser();
        Income income = incomeRepository.findByIdAndUser(id,user)
                .orElseThrow(()-> new IncomeNotFoundException("Income Not Found"));
        incomeRepository.delete(income);
    }
    //Helper Methods
    private User getCurrentUser(){
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User Not Found"));
    }
    public FinancialSummaryResponse getFinancialSummary() {

        User user = getCurrentUser();

        BigDecimal totalIncome =
                incomeRepository.getTotalIncomeByUser(user);

        BigDecimal totalExpense =
                expenseRepository.getTotalExpenseByUser(user);

        BigDecimal balance = totalIncome.subtract(totalExpense);

        return new FinancialSummaryResponse(
                totalIncome,
                totalExpense,
                balance
        );
    }
    public List<MonthlySummaryResponse> getMonthlySummary(int months) {
        User user = getCurrentUser();

        LocalDate from = LocalDate.now()
                .minusMonths(months - 1)
                .withDayOfMonth(1);

        List<Object[]> incomeRows =
                incomeRepository.getMonthlyIncomeByUser(user, from);
        List<Object[]> expenseRows =
                expenseRepository.getMonthlyExpenseByUser(user, from);

        // Map keyed by "YYYY-MM"
        Map<String, BigDecimal> incomeMap = new LinkedHashMap<>();
        for (Object[] row : incomeRows) {
            String key = row[0] + "-" + String.format("%02d", row[1]);
            incomeMap.put(key, (BigDecimal) row[2]);
        }

        Map<String, BigDecimal> expenseMap = new LinkedHashMap<>();
        for (Object[] row : expenseRows) {
            String key = row[0] + "-" + String.format("%02d", row[1]);
            expenseMap.put(key, (BigDecimal) row[2]);
        }

        // Fill all months in range — gaps become zero
        List<MonthlySummaryResponse> result = new ArrayList<>();
        LocalDate cursor = from;
        LocalDate now = LocalDate.now();

        while (!cursor.isAfter(now)) {
            String key = cursor.getYear() + "-" + String.format("%02d", cursor.getMonthValue());
            result.add(new MonthlySummaryResponse(
                    cursor.getYear(),
                    cursor.getMonthValue(),
                    incomeMap.getOrDefault(key, BigDecimal.ZERO),
                    expenseMap.getOrDefault(key, BigDecimal.ZERO)
            ));
            cursor = cursor.plusMonths(1);
        }

        return result;
    }
    private IncomeResponse mapToResponse(Income income){
        IncomeResponse response = new IncomeResponse();
        response.setId(income.getId());
        response.setSource(income.getSource());
        response.setAmount(income.getAmount());
        response.setDescription(income.getDescription());
        response.setDate(income.getDate());
        response.setUserId(income.getUser().getId());

        return response;
    }
}
