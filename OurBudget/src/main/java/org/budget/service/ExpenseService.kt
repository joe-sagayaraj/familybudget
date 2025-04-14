package org.budget.service

import org.budget.categories.BudgetCategory
import java.time.LocalDate

class ExpenseService(private var category: BudgetCategory) {
    companion object  {
    fun add(category: BudgetCategory) {
        println("Expense added for category $category on ${LocalDate.now()}")
    }

    fun remove() {

    }
}
}