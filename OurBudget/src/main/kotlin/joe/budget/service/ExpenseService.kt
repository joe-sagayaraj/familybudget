package joe.budget.service

import joe.budget.data.ExpenseData
import joe.budget.categories.CategoryKey
import java.math.BigDecimal
import java.time.LocalDate

class ExpenseService {
    private val expenses: MutableMap<CategoryKey, MutableList<ExpenseData>> = mutableMapOf()



    fun add(category: CategoryKey, date: LocalDate, price: BigDecimal, currency: String):
            MutableMap<CategoryKey, MutableList<ExpenseData>> {
        expenses.getOrPut(category) { mutableListOf() }.add(ExpenseData(date, price, currency))
        return expenses
    }


    fun removeByDate(category: CategoryKey, date: LocalDate) {
        expenses[category]?.removeAll { it.date == date }
    }

    fun removeFirst(category: CategoryKey, date: LocalDate) {
        val list = expenses[category] ?: return
        val index = list.indexOfFirst { it.date == date }
        if (index != -1) list.removeAt(index)
    }

    fun get(category: CategoryKey): MutableList<ExpenseData>? {
        return expenses[category]
    }

    fun getFirstByDate(category: CategoryKey, date: LocalDate): ExpenseData? {
        val list = expenses[category] ?: return null
        val index = list.indexOfFirst { it.date == date }
        return if (index != -1) list[index] else null
    }

    fun update(category: CategoryKey, date: LocalDate, newPrice: BigDecimal, newCurrency: String) {
        val list = expenses[category] ?: return
        val index = list.indexOfFirst { it.date == date }
        if (index != -1) list[index] = ExpenseData(date, newPrice, newCurrency)
    }

    fun getAll(): Map<CategoryKey, List<ExpenseData>> {
        return expenses.mapValues {
            it.value.toList()
        }
    }

    fun getMonthlySummary(year: Int, month: Int): Map<CategoryKey, BigDecimal> {
        return getAll().mapValues { (_, entries) ->
            entries.filter { it.date.year == year && it.date.monthValue == month }.sumOf { it.price }
        }
    }
}
