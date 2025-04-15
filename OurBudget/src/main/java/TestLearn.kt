import org.budget.api.Expense
import org.budget.categories.BudgetCategory
import org.budget.service.ExpenseService
import java.math.BigDecimal
import java.time.LocalDate

fun main() {
    ExpenseService.add(BudgetCategory.GAS,
        LocalDate.of(2025, 4, 14), BigDecimal(50), "USD" )
    ExpenseService.add(BudgetCategory.HOA,
        LocalDate.of(2025, 4, 12), BigDecimal(202), "USD")
    ExpenseService.add(BudgetCategory.PSE,
        LocalDate.of(2025, 4, 13), BigDecimal(65), "USD")
    println(ExpenseService.add(BudgetCategory.GAS,
        LocalDate.of(2025, 4, 1), BigDecimal(35), "USD"))
}