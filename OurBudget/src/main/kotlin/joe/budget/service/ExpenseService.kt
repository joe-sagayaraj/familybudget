package joe.budget.service

import joe.budget.categories.BudgetCategory
import joe.budget.data.ExpenseData
import java.math.BigDecimal
import java.time.LocalDate

class ExpenseService {
    private val expenses: MutableMap<BudgetCategory, MutableList<ExpenseData>> = mutableMapOf()



    fun add(category: BudgetCategory, date: LocalDate, price: BigDecimal, currency: String):
            MutableMap<BudgetCategory, MutableList<ExpenseData>> {
        expenses.getOrPut(category) { mutableListOf() }.add(ExpenseData(date, price, currency))
        return expenses
    }


    fun removeByDate(category: BudgetCategory, date: LocalDate) {
        expenses[category]?.removeAll { it.date == date }
    }

    fun removeFirst(category: BudgetCategory, date: LocalDate) {
        val list = expenses[category] ?: return
        val index = list.indexOfFirst { it.date == date }
        if (index != -1) list.removeAt(index)
    }
    fun get(category: BudgetCategory): MutableList<ExpenseData>? {
        return expenses[category]
    }
}
