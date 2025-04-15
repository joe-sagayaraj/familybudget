package org.budget.data

import org.budget.categories.BudgetCategory
import java.math.BigDecimal
import java.time.LocalDate

data class ExpenseData(val date: LocalDate, val price: BigDecimal, val currency: String?) {

}
