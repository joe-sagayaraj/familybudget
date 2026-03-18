import joe.budget.api.Expense
import joe.budget.categories.BudgetCategory
import java.math.BigDecimal
import java.time.LocalDate

fun main() {
    val expense = Expense()
    expense.add(BudgetCategory.GAS, LocalDate.of(2025, 4, 14), BigDecimal(50))
    expense.add(BudgetCategory.HOA, LocalDate.of(2025, 4, 12), BigDecimal(202))
    expense.add(BudgetCategory.PSE, LocalDate.of(2025, 4, 13), BigDecimal(65))
    expense.add(BudgetCategory.GAS, LocalDate.of(2025, 4, 1), BigDecimal(35))
    println(expense.get(BudgetCategory.GAS))
}