package joe.budget.repository

import joe.budget.categories.CategoryKey
import joe.budget.data.ExpenseData
import java.time.LocalDate

class InMemoryExpenseRepository : ExpenseRepository {
    private val store: MutableMap<CategoryKey, MutableList<ExpenseData>> =
        mutableMapOf()

    override fun save(category: CategoryKey, data: ExpenseData) {
        store.getOrPut(category) { mutableListOf() }.add(data)
    }

    override fun findByCategory(category: CategoryKey): List<ExpenseData>? =
        store[category]

    override fun findAll(): Map<CategoryKey, List<ExpenseData>> =
        store.mapValues { it.value.toList() }

    override fun deleteByDate(category: CategoryKey, date: LocalDate) {
        store[category]?.removeAll { it.date == date }
    }

    override fun deleteFirst(category: CategoryKey, date: LocalDate) {
        val list = store[category] ?: return
        val index = list.indexOfFirst { it.date == date }
        if (index != -1) list.removeAt(index)
    }

    override fun update(category: CategoryKey, date: LocalDate, newData: ExpenseData) {
        val list = store[category] ?: return
        val index = list.indexOfFirst { it.date == date }
        if (index != -1) list[index] = newData
    }

    override fun recharacterize(from: CategoryKey, entry: ExpenseData, to: CategoryKey) {
        store[from]?.remove(entry)
        store.getOrPut(to) { mutableListOf() }.add(entry)
    }
}