package joe.budget.ui

import joe.budget.categories.BudgetCategory
import joe.budget.categories.BudgetGroup

class CategoryRegistry {
    private val builtIn: List<BudgetCategory> = BudgetCategory.values().toList()
    private val custom: MutableList<CustomCategory> = mutableListOf()

    data class CustomCategory(val name: String, val group: BudgetGroup)

    fun allBuiltIn(): List<BudgetCategory> = builtIn

    fun allCustom(): List<CustomCategory> = custom.toList()

    fun addCustom(name: String, group: BudgetGroup) {
        custom.add(CustomCategory(name, group))
    }
}
