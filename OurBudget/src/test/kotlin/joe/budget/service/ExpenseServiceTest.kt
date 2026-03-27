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

    // --update--

    @Test
    fun `update changes price of matching entry` () {
        val date = LocalDate.of(2025,4,1)
        //val updatedDate = LocalDate.of(2025,10,1)
        service.add(BudgetCategory.GAS, date, BigDecimal("50.00"), "USD")
        service.update(BudgetCategory.GAS, date, BigDecimal("100.00"), "INR")
        val updatedExpenseData = service.get(BudgetCategory.GAS)?.get(0)
        assertEquals(BigDecimal("100.00"), updatedExpenseData?.price)
        assertEquals(date, updatedExpenseData?.date)
        assertEquals("INR", updatedExpenseData?.currency)
    }

    @Test
    fun `update on non-existent category does nothing` () {
        val date = LocalDate.of(2025, 10,1)
        service.add(BudgetCategory.DINING, date, BigDecimal("50.00"), "USD")
        service.update(BudgetCategory.GAS, date, BigDecimal("55.00"), "INR")
        val expenseData = service.get(BudgetCategory.DINING)
        assertNotEquals(BigDecimal("55.00"),expenseData?.get(0)?.price)
        assertNotEquals("INR",expenseData?.get(0)?.currency)
    }

    @Test
    fun `update only changes the first matching entry` () {
        val date = LocalDate.of(2025, 10,1)
        service.add(BudgetCategory.DINING, date, BigDecimal("50.00"), "USD")
        service.add(BudgetCategory.DINING, date, BigDecimal("100.00"), "INR")
        service.update(BudgetCategory.DINING, date, BigDecimal("55.00"), "AUD")
        val firstUpdatedData = service.get(BudgetCategory.DINING)
        val secondUpdatedData = service.get(BudgetCategory.DINING)
        assertEquals(BigDecimal("55.00"),firstUpdatedData?.get(0)?.price)
        assertNotEquals(BigDecimal("55.00"),firstUpdatedData?.get(1)?.price)
        assertEquals("AUD",firstUpdatedData?.get(0)?.currency)
        assertNotEquals("AUD",firstUpdatedData?.get(1)?.currency)
        assertEquals("INR", firstUpdatedData?.get(1)?.currency)
    }

    @Test
    fun `get all returns all`() {
        val date = LocalDate.of(2025, 10,1)
        service.add(BudgetCategory.DINING, date, BigDecimal("50.00"), "USD")
        service.add(BudgetCategory.DINING, date, BigDecimal("100.00"), "INR")
        service.add(BudgetCategory.GROCERIES, date, BigDecimal("100.00"), "INR")
        val result = service.getAll()
        assertEquals(2, result.size)
        assertEquals(2, result[BudgetCategory.DINING]?.size)
        assertEquals(1, result[BudgetCategory.GROCERIES]?.size)
    }

    @Test
    fun `get monthly summary returns only the existing categories for the specified period`() {
        val date = LocalDate.of(2025, 10,1)
        service.add(BudgetCategory.DINING, date, BigDecimal("50.00"), "USD")
        service.add(BudgetCategory.DINING, date, BigDecimal("100.00"), "INR")
        service.add(BudgetCategory.GROCERIES, date, BigDecimal("100.00"), "INR")
        val date1 = LocalDate.of(2025, 11,1)
        service.add(BudgetCategory.DINING, date1, BigDecimal("50.00"), "USD")
        service.add(BudgetCategory.DINING, date1, BigDecimal("100.00"), "INR")
        service.add(BudgetCategory.GROCERIES, date1, BigDecimal("100.00"), "INR")
        val date2 = LocalDate.of(2025, 12,1)
        service.add(BudgetCategory.GAS, date2, BigDecimal("50.00"), "USD")
        service.add(BudgetCategory.DINING, date2, BigDecimal("100.00"), "INR")
        service.add(BudgetCategory.DINING, date2, BigDecimal("150.00"), "INR")
        service.add(BudgetCategory.GROCERIES, date2, BigDecimal("100.00"), "INR")
        val result = service.getMonthlySummary(2025, 12)
        assertEquals(3, result.size)
        assertEquals(BigDecimal("50.00"), result[BudgetCategory.GAS])
        assertEquals(BigDecimal("250.00"), result[BudgetCategory.DINING])
    }
}
