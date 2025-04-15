package org.budget.service

import org.budget.categories.BudgetCategory
import org.budget.data.ExpenseData
import java.math.BigDecimal
import java.time.LocalDate

class ExpenseService(private var category: BudgetCategory, private val date: LocalDate = LocalDate.now(),
                     private val price: BigDecimal, private val currency: String = "USD") {

    companion object  {
        @JvmStatic
        public val expenses: MutableMap<BudgetCategory, MutableList<ExpenseData>> = mutableMapOf()
    fun add(category: BudgetCategory, date: LocalDate, price: BigDecimal, currency: String?):
            MutableMap<BudgetCategory, MutableList<ExpenseData>> {
        //println("Expense added for category $category on ${LocalDate.now()}")
        expenses.getOrPut(category) {mutableListOf()}.add(ExpenseData(date, price, currency!!) )
        return expenses
    }

    fun remove() {

    }
    fun get(category: BudgetCategory): MutableList<ExpenseData>? {
        return expenses[category]
    }
}
}