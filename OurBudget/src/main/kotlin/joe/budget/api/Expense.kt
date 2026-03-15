package joe.budget.api

import joe.budget.categories.BudgetCategory
import joe.budget.service.ExpenseService
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