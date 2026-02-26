package com.rajendra.expensemanager.expense;

import com.rajendra.expensemanager.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ExpenseRepository extends JpaRepository<Expense,Long>,
        JpaSpecificationExecutor<Expense> {
    @Query("SELECT DISTINCT e.category FROM Expense e WHERE e.user = :user")
    List<String> findDistinctCategoriesByUser(@Param("user") User user);
    List<Expense> findByUser(User user);
    Optional<Expense> findByIdAndUser(Long id, User user);

    @Query("""
       SELECT COALESCE(SUM(e.amount), 0)
       FROM Expense e
       WHERE e.user = :user
       """)
    BigDecimal getTotalExpenseByUser(User user);
}
