package joe.budget.imports

import joe.budget.categories.CategoryKey
import java.math.BigDecimal
import java.time.LocalDate

data class Transaction(
    val date: LocalDate,
    val payee: String,
    val amount: BigDecimal,
    val category: CategoryKey,
    val status: TransactionStatus
)
