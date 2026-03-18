package joe.budget.data

import joe.budget.categories.BudgetCategory
import java.math.BigDecimal
import java.time.LocalDate

data class ExpenseData(val date: LocalDate, val price: BigDecimal, val currency: String?) {

}
