package joe.budget.service

import joe.budget.categories.BudgetCategory
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDate

class ExpenseServiceTest {

    private lateinit var service: ExpenseService

    @BeforeEach
    fun setup() {
        service = ExpenseService()
    }

    // --- add ---

    @Test
    fun `add creates a new entry for the category`() {
        service.add(BudgetCategory.GAS, LocalDate.of(2025, 4, 1), BigDecimal("50.00"), "USD")

        val result = service.get(BudgetCategory.GAS)
        assertEquals(1, result?.size)
    }

    @Test
    fun `add multiple entries to same category`() {
        service.add(BudgetCategory.GAS, LocalDate.of(2025, 4, 1), BigDecimal("50.00"), "USD")
        service.add(BudgetCategory.GAS, LocalDate.of(2025, 4, 14), BigDecimal("35.00"), "USD")

        val result = service.get(BudgetCategory.GAS)
        assertEquals(2, result?.size)
    }

    @Test
    fun `add entries to different categories`() {
        service.add(BudgetCategory.GAS, LocalDate.of(2025, 4, 1), BigDecimal("50.00"), "USD")
        service.add(BudgetCategory.GROCERIES, LocalDate.of(2025, 4, 2), BigDecimal("120.00"), "USD")

        assertEquals(1, service.get(BudgetCategory.GAS)?.size)
        assertEquals(1, service.get(BudgetCategory.GROCERIES)?.size)
    }

    // --- get ---

    @Test
    fun `get returns null for category with no expenses`() {
        assertNull(service.get(BudgetCategory.NETFLIX))
    }

    @Test
    fun `get returns correct expense data`() {
        val date = LocalDate.of(2025, 4, 1)
        service.add(BudgetCategory.GAS, date, BigDecimal("50.00"), "USD")

        val result = service.get(BudgetCategory.GAS)!!
        assertEquals(date, result[0].date)
        assertEquals(BigDecimal("50.00"), result[0].price)
        assertEquals("USD", result[0].currency)
    }

    // --- removeByDate ---

    @Test
    fun `removeByDate removes all entries matching date`() {
        val date = LocalDate.of(2025, 4, 1)
        service.add(BudgetCategory.GAS, date, BigDecimal("50.00"), "USD")
        service.add(BudgetCategory.GAS, date, BigDecimal("30.00"), "USD")
        service.add(BudgetCategory.GAS, LocalDate.of(2025, 4, 14), BigDecimal("35.00"), "USD")

        service.removeByDate(BudgetCategory.GAS, date)

        val result = service.get(BudgetCategory.GAS)
        assertEquals(1, result?.size)
        assertEquals(LocalDate.of(2025, 4, 14), result?.get(0)?.date)
    }

    @Test
    fun `removeByDate on non-existent category does nothing`() {
        service.removeByDate(BudgetCategory.GAS, LocalDate.of(2025, 4, 1))
        assertNull(service.get(BudgetCategory.GAS))
    }

    // --- removeFirst ---

    @Test
    fun `removeFirst removes only the first matching entry`() {
        val date = LocalDate.of(2025, 4, 1)
        service.add(BudgetCategory.GAS, date, BigDecimal("50.00"), "USD")
        service.add(BudgetCategory.GAS, date, BigDecimal("30.00"), "USD")

        service.removeFirst(BudgetCategory.GAS, date)

        val result = service.get(BudgetCategory.GAS)
        assertEquals(1, result?.size)
        assertEquals(BigDecimal("30.00"), result?.get(0)?.price)
    }

    @Test
    fun `removeFirst on non-existent category does nothing`() {
        service.removeFirst(BudgetCategory.GAS, LocalDate.of(2025, 4, 1))
        assertNull(service.get(BudgetCategory.GAS))
    }

    @Test
    fun `removeFirst when no entry matches date does nothing`() {
        service.add(BudgetCategory.GAS, LocalDate.of(2025, 4, 1), BigDecimal("50.00"), "USD")

        service.removeFirst(BudgetCategory.GAS, LocalDate.of(2025, 5, 1))

        assertEquals(1, service.get(BudgetCategory.GAS)?.size)
    }
}
