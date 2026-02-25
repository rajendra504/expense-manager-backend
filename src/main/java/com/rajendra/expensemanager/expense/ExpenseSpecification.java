package com.rajendra.expensemanager.expense;

import com.rajendra.expensemanager.user.User;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ExpenseSpecification {

    public static Specification<Expense> filterExpenses(
            User user,
            String category,
            BigDecimal minAmount,
            BigDecimal maxAmount,
            String search
    ) {
        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            // Only logged-in user's data
            predicates.add(cb.equal(root.get("user"), user));

            if (category != null) {
                predicates.add(cb.equal(root.get("category"), category));
            }

            if (minAmount != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("amount"), minAmount));
            }

            if (maxAmount != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("amount"), maxAmount));
            }

            if (search != null) {
                predicates.add(
                        cb.or(
                                cb.like(cb.lower(root.get("title")), "%" + search.toLowerCase() + "%"),
                                cb.like(cb.lower(root.get("description")), "%" + search.toLowerCase() + "%")
                        )
                );
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
