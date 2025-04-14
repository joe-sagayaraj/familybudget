package org.budget.api

import org.budget.categories.BudgetCategory
import org.budget.service.ExpenseService
import java.sql.Time
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Date

class Expense() {
    fun add(category: BudgetCategory) {
        ExpenseService.add(category)
    }

    fun remove() {

    }
}