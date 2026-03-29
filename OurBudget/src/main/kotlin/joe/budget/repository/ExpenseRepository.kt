package joe.budget.repository

import joe.budget.categories.CategoryKey
import joe.budget.data.ExpenseData
import java.time.LocalDate

interface ExpenseRepository {
    fun save(category: CategoryKey, data: ExpenseData)
    fun findByCategory(category: CategoryKey): List<ExpenseData>?
    fun findAll(): Map<CategoryKey, List<ExpenseData>>
    fun deleteByDate(category: CategoryKey, date: LocalDate)
    fun deleteFirst(category: CategoryKey, date: LocalDate)
    fun update(category: CategoryKey, date: LocalDate, newData: ExpenseData)
    fun recharacterize(from: CategoryKey, entry: ExpenseData, to: CategoryKey)
}