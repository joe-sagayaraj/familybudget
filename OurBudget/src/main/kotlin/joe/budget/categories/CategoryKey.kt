package joe.budget.categories

sealed class CategoryKey {
    data class BuiltIn(val category: BudgetCategory) : CategoryKey()
    data class Custom(val name: String, val group: BudgetGroup) : CategoryKey()
}
