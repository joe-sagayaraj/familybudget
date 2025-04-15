package org.budget.api

import org.budget.categories.BudgetCategory
import org.budget.service.ExpenseService
import java.math.BigDecimal
import java.time.LocalDate

class Expense() {
    fun add(category: BudgetCategory) {
        print(ExpenseService.Companion.add(category, LocalDate.now(), BigDecimal(100), "USD"))
    }

    fun remove() {

    }

    companion object {
        fun get(): String = "Self referencing companion object"
    }
}