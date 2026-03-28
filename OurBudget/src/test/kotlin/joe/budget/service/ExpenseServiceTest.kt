package joe.budget.service

import joe.budget.categories.BudgetCategory
import joe.budget.categories.CategoryKey
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDate

class ExpenseServiceTest {

    private lateinit var service: ExpenseService

    private val GAS = CategoryKey.BuiltIn(BudgetCategory.GAS)
    private val GROCERIES = CategoryKey.BuiltIn(BudgetCategory.GROCERIES)
    private val NETFLIX = CategoryKey.BuiltIn(BudgetCategory.NETFLIX)
    private val DINING = CategoryKey.BuiltIn(BudgetCategory.DINING)

    @BeforeEach
    fun setup() {
        service = ExpenseService()
    }

    // --- add ---

    @Test
    fun `add creates a new entry for the category`() {
        service.add(GAS, LocalDate.of(2025, 4, 1), BigDecimal("50.00"), "USD")

        val result = service.get(GAS)
        assertEquals(1, result?.size)
    }

    @Test
    fun `add multiple entries to same category`() {
        service.add(GAS, LocalDate.of(2025, 4, 1), BigDecimal("50.00"), "USD")
        service.add(GAS, LocalDate.of(2025, 4, 14), BigDecimal("35.00"), "USD")

        val result = service.get(GAS)
        assertEquals(2, result?.size)
    }

    @Test
    fun `add entries to different categories`() {
        service.add(GAS, LocalDate.of(2025, 4, 1), BigDecimal("50.00"), "USD")
        service.add(GROCERIES, LocalDate.of(2025, 4, 2), BigDecimal("120.00"), "USD")

        assertEquals(1, service.get(GAS)?.size)
        assertEquals(1, service.get(GROCERIES)?.size)
    }

    // --- get ---

    @Test
    fun `get returns null for category with no expenses`() {
        assertNull(service.get(NETFLIX))
    }

    @Test
    fun `get returns correct expense data`() {
        val date = LocalDate.of(2025, 4, 1)
        service.add(GAS, date, BigDecimal("50.00"), "USD")

        val result = service.get(GAS)!!
        assertEquals(date, result[0].date)
        assertEquals(BigDecimal("50.00"), result[0].price)
        assertEquals("USD", result[0].currency)
    }

    // --- removeByDate ---

    @Test
    fun `removeByDate removes all entries matching date`() {
        val date = LocalDate.of(2025, 4, 1)
        service.add(GAS, date, BigDecimal("50.00"), "USD")
        service.add(GAS, date, BigDecimal("30.00"), "USD")
        service.add(GAS, LocalDate.of(2025, 4, 14), BigDecimal("35.00"), "USD")

        service.removeByDate(GAS, date)

        val result = service.get(GAS)
        assertEquals(1, result?.size)
        assertEquals(LocalDate.of(2025, 4, 14), result?.get(0)?.date)
    }

    @Test
    fun `removeByDate on non-existent category does nothing`() {
        service.removeByDate(GAS, LocalDate.of(2025, 4, 1))
        assertNull(service.get(GAS))
    }

    // --- removeFirst ---

    @Test
    fun `removeFirst removes only the first matching entry`() {
        val date = LocalDate.of(2025, 4, 1)
        service.add(GAS, date, BigDecimal("50.00"), "USD")
        service.add(GAS, date, BigDecimal("30.00"), "USD")

        service.removeFirst(GAS, date)

        val result = service.get(GAS)
        assertEquals(1, result?.size)
        assertEquals(BigDecimal("30.00"), result?.get(0)?.price)
    }

    @Test
    fun `removeFirst on non-existent category does nothing`() {
        service.removeFirst(GAS, LocalDate.of(2025, 4, 1))
        assertNull(service.get(GAS))
    }

    @Test
    fun `removeFirst when no entry matches date does nothing`() {
        service.add(GAS, LocalDate.of(2025, 4, 1), BigDecimal("50.00"), "USD")

        service.removeFirst(GAS, LocalDate.of(2025, 5, 1))

        assertEquals(1, service.get(GAS)?.size)
    }

    // --update--

    @Test
    fun `update changes price of matching entry` () {
        val date = LocalDate.of(2025,4,1)
        service.add(GAS, date, BigDecimal("50.00"), "USD")
        service.update(GAS, date, BigDecimal("100.00"), "INR")
        val updatedExpenseData = service.get(GAS)?.get(0)
        assertEquals(BigDecimal("100.00"), updatedExpenseData?.price)
        assertEquals(date, updatedExpenseData?.date)
        assertEquals("INR", updatedExpenseData?.currency)
    }

    @Test
    fun `update on non-existent category does nothing` () {
        val date = LocalDate.of(2025, 10,1)
        service.add(DINING, date, BigDecimal("50.00"), "USD")
        service.update(GAS, date, BigDecimal("55.00"), "INR")
        val expenseData = service.get(DINING)
        assertNotEquals(BigDecimal("55.00"),expenseData?.get(0)?.price)
        assertNotEquals("INR",expenseData?.get(0)?.currency)
    }

    @Test
    fun `update only changes the first matching entry` () {
        val date = LocalDate.of(2025, 10,1)
        service.add(DINING, date, BigDecimal("50.00"), "USD")
        service.add(DINING, date, BigDecimal("100.00"), "INR")
        service.update(DINING, date, BigDecimal("55.00"), "AUD")
        val firstUpdatedData = service.get(DINING)
        val secondUpdatedData = service.get(DINING)
        assertEquals(BigDecimal("55.00"),firstUpdatedData?.get(0)?.price)
        assertNotEquals(BigDecimal("55.00"),firstUpdatedData?.get(1)?.price)
        assertEquals("AUD",firstUpdatedData?.get(0)?.currency)
        assertNotEquals("AUD",firstUpdatedData?.get(1)?.currency)
        assertEquals("INR", firstUpdatedData?.get(1)?.currency)
    }

    @Test
    fun `get all returns all`() {
        val date = LocalDate.of(2025, 10,1)
        service.add(DINING, date, BigDecimal("50.00"), "USD")
        service.add(DINING, date, BigDecimal("100.00"), "INR")
        service.add(GROCERIES, date, BigDecimal("100.00"), "INR")
        val result = service.getAll()
        assertEquals(2, result.size)
        assertEquals(2, result[DINING]?.size)
        assertEquals(1, result[GROCERIES]?.size)
    }

    @Test
    fun `get monthly summary returns only the existing categories for the specified period`() {
        val date = LocalDate.of(2025, 10,1)
        service.add(DINING, date, BigDecimal("50.00"), "USD")
        service.add(DINING, date, BigDecimal("100.00"), "INR")
        service.add(GROCERIES, date, BigDecimal("100.00"), "INR")
        val date1 = LocalDate.of(2025, 11,1)
        service.add(DINING, date1, BigDecimal("50.00"), "USD")
        service.add(DINING, date1, BigDecimal("100.00"), "INR")
        service.add(GROCERIES, date1, BigDecimal("100.00"), "INR")
        val date2 = LocalDate.of(2025, 12,1)
        service.add(GAS, date2, BigDecimal("50.00"), "USD")
        service.add(DINING, date2, BigDecimal("100.00"), "INR")
        service.add(DINING, date2, BigDecimal("150.00"), "INR")
        service.add(GROCERIES, date2, BigDecimal("100.00"), "INR")
        val result = service.getMonthlySummary(2025, 12)
        assertEquals(3, result.size)
        assertEquals(BigDecimal("50.00"), result[GAS])
        assertEquals(BigDecimal("250.00"), result[DINING])
    }
}
