import joe.budget.api.Expense
import joe.budget.categories.BudgetCategory
import joe.budget.categories.CategoryKey
import java.math.BigDecimal
import java.time.LocalDate

fun main() {
    val expense = Expense()
    expense.add(CategoryKey.BuiltIn(BudgetCategory.GAS), LocalDate.of(2025, 4, 14), BigDecimal(50))
    expense.add(CategoryKey.BuiltIn(BudgetCategory.HOA), LocalDate.of(2025, 4, 12), BigDecimal(202))
    expense.add(CategoryKey.BuiltIn(BudgetCategory.PSE), LocalDate.of(2025, 4, 13), BigDecimal(65))
    expense.add(CategoryKey.BuiltIn(BudgetCategory.GAS), LocalDate.of(2025, 4, 1), BigDecimal(35))
    println(expense.get(CategoryKey.BuiltIn(BudgetCategory.GAS)))
}