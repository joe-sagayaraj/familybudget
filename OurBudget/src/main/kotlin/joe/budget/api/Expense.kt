package joe.budget.api

import joe.budget.categories.BudgetCategory
import joe.budget.data.ExpenseData
import joe.budget.service.ExpenseService
import java.math.BigDecimal
import java.time.LocalDate

class Expense {
    private val service = ExpenseService()

    fun add(category: BudgetCategory, date: LocalDate = LocalDate.now(), price: BigDecimal, currency: String = "USD") {
        service.add(category, date, price, currency)
    }

    fun get(category: BudgetCategory): List<ExpenseData>? =
        service.get(category)


    fun removeByDate(category: BudgetCategory, date: LocalDate) {
        service.removeByDate(category, date)
    }

    fun removeFirst(category: BudgetCategory, date: LocalDate) {
        service.removeFirst(category, date)
    }

    fun update(category: BudgetCategory, date: LocalDate, newPrice: BigDecimal, newCurrency: String) {
        service.update(category, date, newPrice, newCurrency)
    }

    fun getAll(): Map<BudgetCategory, List<ExpenseData>> =
        service.getAll()

    fun getMonthlySummary(year: Int, month: Int): Map<BudgetCategory, BigDecimal> =
        service.getMonthlySummary(year, month)
}