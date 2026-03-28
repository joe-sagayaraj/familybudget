package joe.budget.api

import joe.budget.categories.BudgetCategory
import joe.budget.categories.CategoryKey
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDate

class ExpenseTest {

    private lateinit var expense: Expense

    private val GAS = CategoryKey.BuiltIn(BudgetCategory.GAS)
    private val GROCERIES = CategoryKey.BuiltIn(BudgetCategory.GROCERIES)

    @BeforeEach
    fun setup() {
        expense = Expense()
    }

    @Test
    fun `add and get expense`() {
        val date = LocalDate.of(2025, 4, 1)
        expense.add(GROCERIES, date, BigDecimal("120.00"))

        val result = expense.get(GROCERIES)
        assertEquals(1, result?.size)
        assertEquals(date, result?.get(0)?.date)
    }

    @Test
    fun `get returns null for category with no expenses`() {
        assertNull(expense.get(GROCERIES))
    }

    @Test
    fun `removeByDate removes entries via API`() {
        val date = LocalDate.of(2025, 4, 1)
        expense.add(GAS, date, BigDecimal("50.00"))
        expense.add(GAS, LocalDate.of(2025, 4, 14), BigDecimal("35.00"))

        expense.removeByDate(GAS, date)

        assertEquals(1, expense.get(GAS)?.size)
    }

    @Test
    fun `removeFirst removes only first entry via API`() {
        val date = LocalDate.of(2025, 4, 1)
        expense.add(GAS, date, BigDecimal("50.00"))
        expense.add(GAS, date, BigDecimal("30.00"))

        expense.removeFirst(GAS, date)

        assertEquals(1, expense.get(GAS)?.size)
    }
}
