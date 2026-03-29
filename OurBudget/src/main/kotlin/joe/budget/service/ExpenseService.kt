package joe.budget.service

import joe.budget.repository.ExpenseRepository
import joe.budget.repository.InMemoryExpenseRepository
import joe.budget.categories.CategoryKey
import joe.budget.data.ExpenseData
import java.math.BigDecimal
import java.time.LocalDate

class ExpenseService(
    private val repository: ExpenseRepository = InMemoryExpenseRepository()
) {
    fun add(category: CategoryKey, date: LocalDate, price: BigDecimal, currency: String, payee: String? = null) {
        repository.save(category, ExpenseData(date, price, currency, payee))
    }

    fun removeByDate(category: CategoryKey, date: LocalDate) {
        repository.deleteByDate(category, date)
    }

    fun removeFirst(category: CategoryKey, date: LocalDate) {
        repository.deleteFirst(category, date)
    }

    fun get(category: CategoryKey): List<ExpenseData>? =
        repository.findByCategory(category)

    fun getFirstByDate(category: CategoryKey, date: LocalDate): ExpenseData? =
        repository.findByCategory(category)?.firstOrNull { it.date == date }

    fun update(category: CategoryKey, date: LocalDate, newPrice: BigDecimal,
               newCurrency: String) {
        repository.update(category, date, ExpenseData(date, newPrice, newCurrency))
    }

    fun updateEntry(category: CategoryKey, oldDate: LocalDate, newDate: LocalDate, newPrice: BigDecimal, newCurrency:
    String) {
        repository.deleteFirst(category, oldDate)
        repository.save(category, ExpenseData(newDate, newPrice, newCurrency))
    }

    fun getAll(): Map<CategoryKey, List<ExpenseData>> =
        repository.findAll()

    fun recharacterize(from: CategoryKey, entry: ExpenseData, to: CategoryKey) {
        repository.recharacterize(from, entry, to)
    }

    fun getMonthlySummary(year: Int, month: Int): Map<CategoryKey, BigDecimal> =
        getAll().mapValues { (_, entries) ->
            entries.filter { it.date.year == year && it.date.monthValue == month
            }.sumOf { it.price }
        }
}