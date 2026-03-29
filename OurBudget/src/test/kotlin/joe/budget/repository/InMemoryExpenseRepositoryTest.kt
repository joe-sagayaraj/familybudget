package joe.budget.repository

import joe.budget.categories.BudgetCategory
import joe.budget.categories.CategoryKey
import joe.budget.data.ExpenseData
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDate

class InMemoryExpenseRepositoryTest {

    private lateinit var repo: InMemoryExpenseRepository
    private val GAS = CategoryKey.BuiltIn(BudgetCategory.GAS)
    private val GROCERIES = CategoryKey.BuiltIn(BudgetCategory.GROCERIES)
    private val date = LocalDate.of(2025, 4, 1)

    @BeforeEach
    fun setup() {
        repo = InMemoryExpenseRepository()
    }

    @Test
    fun `save and findByCategory returns saved entry`() {
        repo.save(GAS, ExpenseData(date, BigDecimal("50.00"), "USD"))
        val result = repo.findByCategory(GAS)
        assertEquals(1, result?.size)
        assertEquals(BigDecimal("50.00"), result?.get(0)?.price)
    }

    @Test
    fun `findByCategory returns null for unknown category`() {
        assertNull(repo.findByCategory(GAS))
    }

    @Test
    fun `findAll returns all categories`() {
        repo.save(GAS, ExpenseData(date, BigDecimal("50.00"), "USD"))
        repo.save(GROCERIES, ExpenseData(date, BigDecimal("100.00"), "USD"))
        assertEquals(2, repo.findAll().size)
    }

    @Test
    fun `deleteByDate removes all matching entries`() {
        repo.save(GAS, ExpenseData(date, BigDecimal("50.00"), "USD"))
        repo.save(GAS, ExpenseData(date, BigDecimal("30.00"), "USD"))
        repo.deleteByDate(GAS, date)
        assertEquals(0, repo.findByCategory(GAS)?.size)
    }

    @Test
    fun `deleteFirst removes only the first matching entry`() {
        repo.save(GAS, ExpenseData(date, BigDecimal("50.00"), "USD"))
        repo.save(GAS, ExpenseData(date, BigDecimal("30.00"), "USD"))
        repo.deleteFirst(GAS, date)
        val result = repo.findByCategory(GAS)
        assertEquals(1, result?.size)
        assertEquals(BigDecimal("30.00"), result?.get(0)?.price)
    }

    @Test
    fun `update replaces first matching entry`() {
        repo.save(GAS, ExpenseData(date, BigDecimal("50.00"), "USD"))
        repo.update(GAS, date, ExpenseData(date, BigDecimal("99.00"), "EUR"))
        val result = repo.findByCategory(GAS)?.get(0)
        assertEquals(BigDecimal("99.00"), result?.price)
        assertEquals("EUR", result?.currency)
    }
}