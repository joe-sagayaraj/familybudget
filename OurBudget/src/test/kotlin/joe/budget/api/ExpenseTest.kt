package joe.budget.api

import joe.budget.categories.BudgetCategory
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDate

class ExpenseTest {

    private lateinit var expense: Expense

    @BeforeEach
    fun setup() {
        expense = Expense()
    }

    @Test
    fun `add and get expense`() {
        val date = LocalDate.of(2025, 4, 1)
        expense.add(BudgetCategory.GROCERIES, date, BigDecimal("120.00"))

        val result = expense.get(BudgetCategory.GROCERIES)
        assertEquals(1, result?.size)
        assertEquals(date, result?.get(0)?.date)
    }

    @Test
    fun `get returns null for category with no expenses`() {
        assertNull(expense.get(BudgetCategory.GROCERIES))
    }

    @Test
    fun `removeByDate removes entries via API`() {
        val date = LocalDate.of(2025, 4, 1)
        expense.add(BudgetCategory.GAS, date, BigDecimal("50.00"))
        expense.add(BudgetCategory.GAS, LocalDate.of(2025, 4, 14), BigDecimal("35.00"))

        expense.removeByDate(BudgetCategory.GAS, date)

        assertEquals(1, expense.get(BudgetCategory.GAS)?.size)
    }

    @Test
    fun `removeFirst removes only first entry via API`() {
        val date = LocalDate.of(2025, 4, 1)
        expense.add(BudgetCategory.GAS, date, BigDecimal("50.00"))
        expense.add(BudgetCategory.GAS, date, BigDecimal("30.00"))

        expense.removeFirst(BudgetCategory.GAS, date)

        assertEquals(1, expense.get(BudgetCategory.GAS)?.size)
    }
}
