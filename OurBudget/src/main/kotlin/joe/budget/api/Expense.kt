package joe.budget.api

import joe.budget.categories.CategoryKey
import joe.budget.data.ExpenseData
import joe.budget.service.ExpenseService
import java.math.BigDecimal
import java.time.LocalDate

class Expense {
    private val service = ExpenseService()

    fun add(category: CategoryKey, date: LocalDate = LocalDate.now(), price: BigDecimal, currency: String = "USD", payee: String? = null) {
        service.add(category, date, price, currency, payee)
    }

    fun get(category: CategoryKey): List<ExpenseData>? =
        service.get(category)


    fun removeByDate(category: CategoryKey, date: LocalDate) {
        service.removeByDate(category, date)
    }

    fun removeFirst(category: CategoryKey, date: LocalDate) {
        service.removeFirst(category, date)
    }

    fun update(category: CategoryKey, date: LocalDate, newPrice: BigDecimal, newCurrency: String) {
        service.update(category, date, newPrice, newCurrency)
    }

    fun updateEntry(category: CategoryKey, oldDate: LocalDate, newDate: LocalDate, newPrice: BigDecimal, newCurrency:
    String) {
        service.updateEntry(category, oldDate, newDate, newPrice, newCurrency)
    }

    fun getAll(): Map<CategoryKey, List<ExpenseData>> =
        service.getAll()

    fun recharacterize(from: CategoryKey, entry: joe.budget.data.ExpenseData, to: CategoryKey) {
        service.recharacterize(from, entry, to)
    }

    fun getMonthlySummary(year: Int, month: Int): Map<CategoryKey, BigDecimal> =
        service.getMonthlySummary(year, month)

}