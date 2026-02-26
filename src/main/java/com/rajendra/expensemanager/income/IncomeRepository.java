package com.rajendra.expensemanager.income;

import com.rajendra.expensemanager.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface IncomeRepository extends JpaRepository<Income,Long> {
    Page<Income> findByUser(User user, Pageable pageable);
    Optional<Income> findByIdAndUser(Long id, User user);

    @Query("""
           SELECT COALESCE(SUM(i.amount), 0)
           FROM Income i
           WHERE i.user = :user
           """)
    BigDecimal getTotalIncomeByUser(User user);
}
