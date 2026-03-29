package joe.budget.imports

import joe.budget.api.Expense
import joe.budget.categories.BudgetCategory
import joe.budget.categories.CategoryKey
import java.io.File
import java.math.BigDecimal
import java.time.LocalDate
import java.util.Properties

class ImportService(private val expense: Expense) {

    // NOTE: pending and seen are in-memory only — reset on restart.
    // Will persist to file/db in a future phase.
    private val pending = mutableListOf<Transaction>()
    private val seen = mutableSetOf<TxKey>()
    private val importer = CsvImporter()
    private val matcher = CategoryMatcher()
    val thresholdDays: Long

    init {
        val config = Properties()
        val configFile = File("config/import-config.properties")
        if (configFile.exists()) {
            configFile.inputStream().use { config.load(it) }
        }
        thresholdDays = config.getProperty("approval.threshold.days", "3").toLongOrNull() ?: 3L
    }

    fun import(file: File): ImportSummary {
        val parsed = importer.parse(file)
        var imported = 0
        var duplicates = 0
        var uncategorized = 0
        val warnings = parsed.errors.toMutableList()

        for (row in parsed.rows) {
            val key = TxKey(row.date, row.payee, row.amount)
            if (seen.contains(key)) {
                warnings.add("Duplicate skipped: ${row.date} | ${row.payee} | \$${row.amount}")
                duplicates++
                continue
            }
            seen.add(key)
            val category = matcher.match(row.payee)
            if (category == CategoryKey.BuiltIn(BudgetCategory.UNCATEGORIZED)) uncategorized++
            pending.add(Transaction(row.date, row.payee, row.amount, category, TransactionStatus.PENDING))
            imported++
        }

        val approved = autoApprove()
        return ImportSummary(imported, duplicates, uncategorized, approved, warnings)
    }

    private fun autoApprove(): Int {
        val cutoff = LocalDate.now().minusDays(thresholdDays)
        var count = 0
        val iter = pending.iterator()
        while (iter.hasNext()) {
            val tx = iter.next()
            if (!tx.date.isAfter(cutoff)) {
                expense.add(tx.category, tx.date, tx.amount, payee = tx.payee)
                iter.remove()
                count++
            }
        }
        return count
    }

    fun pendingCount(): Int = pending.size
}

data class ImportSummary(
    val imported: Int,
    val duplicates: Int,
    val uncategorized: Int,
    val autoApproved: Int,
    val warnings: List<String>
)

private data class TxKey(val date: LocalDate, val payee: String, val amount: BigDecimal)
