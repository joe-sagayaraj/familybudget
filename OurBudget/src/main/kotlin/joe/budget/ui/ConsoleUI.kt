package joe.budget.ui

import joe.budget.api.Expense
import joe.budget.categories.BudgetCategory
import joe.budget.categories.BudgetGroup
import java.math.BigDecimal
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Scanner

class ConsoleUI(
    private val expense: Expense,
    private val registry: CategoryRegistry,
    private val input: Scanner = Scanner(System.`in`)
) {
    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    private val monthFormatter = DateTimeFormatter.ofPattern("yyyy-MM")

    fun run() {
        while (true) {
            println("\n=== Family Budget ===")
            println("1. Add expense")
            println("2. View expenses")
            println("3. Modify expense")
            println("4. Manage categories")
            println("0. Exit")
            when (readInt("> ", 0, 4)) {
                1 -> addExpense()
                2 -> viewExpenses()
                3 -> modifyExpense()
                4 -> manageCategories()
                0 -> { println("Goodbye!"); return }
            }
        }
    }

    // --- Add Expense ---

    private fun addExpense() {
        println("\n-- Add Expense --")
        val category = promptBuiltInCategory() ?: return
        val amount = promptAmount() ?: return
        val date = promptDate(LocalDate.now())
        expense.add(category, date, amount)
        println("Added ${category.name} expense of \$$amount on $date.")
    }

    // --- View Expenses ---

    private fun viewExpenses() {
        println("\n-- View Expenses --")
        println("1. By category")
        println("2. Monthly summary")
        println("0. Back")
        when (readInt("> ", 0, 2)) {
            1 -> viewByCategory()
            2 -> viewMonthlySummary()
        }
    }

    private fun viewByCategory() {
        println("\n-- View by Category --")
        val category = promptBuiltInCategory() ?: return
        val entries = expense.get(category)
        if (entries.isNullOrEmpty()) {
            println("No expenses recorded for ${category.name}.")
            return
        }
        println("\n${category.name} — ${entries.size} entries")
        entries.forEach { println("  ${it.date}  \$${it.price}  ${it.currency ?: ""}") }
        val total = entries.sumOf { it.price }
        println("  " + "─".repeat(30))
        println("  Total: \$$total")
    }

    private fun viewMonthlySummary() {
        println("\n-- Monthly Summary --")
        val (year, month) = promptMonth()
        val summary = expense.getMonthlySummary(year, month)
        val monthLabel = LocalDate.of(year, month, 1).format(DateTimeFormatter.ofPattern("MMMM yyyy"))
        if (summary.isEmpty()) {
            println("No expenses recorded for $monthLabel.")
            return
        }
        println("\nMonthly summary for $monthLabel:")
        summary.forEach { (cat, total) -> println("  %-20s \$%s".format(cat.name, total)) }
        val grandTotal = summary.values.sumOf { it }
        println("  " + "─".repeat(30))
        println("  %-20s \$%s".format("Grand total:", grandTotal))
    }

    // --- Modify Expense ---

    private fun modifyExpense() {
        println("\n-- Modify Expense --")
        val category = promptBuiltInCategory() ?: return
        val entries = expense.get(category)
        if (entries.isNullOrEmpty()) {
            println("No expenses recorded for ${category.name}.")
            return
        }
        println("\n${category.name} — entries:")
        entries.forEachIndexed { i, e -> println("  ${i + 1}. ${e.date}  \$${e.price}  ${e.currency ?: ""}") }
        val index = readInt("Select entry number (or 0 to cancel): ", 0, entries.size)
        if (index == 0) return
        val selected = entries[index - 1]

        println("1. Update")
        println("2. Delete")
        println("0. Cancel")
        when (readInt("> ", 0, 2)) {
            1 -> {
                val newAmount = promptAmountOrKeep(selected.price) ?: return
                val newDate = promptDateOrKeep(selected.date)
                expense.removeFirst(category, selected.date)
                expense.add(category, newDate, newAmount)
                println("Updated.")
            }
            2 -> {
                print("Delete entry on ${selected.date} for \$${selected.price}? (y/n): ")
                if (input.nextLine().trim().lowercase() == "y") {
                    expense.removeFirst(category, selected.date)
                    println("Deleted.")
                } else {
                    println("Cancelled.")
                }
            }
        }
    }

    // --- Manage Categories ---

    private fun manageCategories() {
        println("\n-- Manage Categories --")
        println("1. Add custom category (this session only)")
        println("2. List all categories")
        println("0. Back")
        when (readInt("> ", 0, 2)) {
            1 -> addCustomCategory()
            2 -> listAllCategories()
        }
    }

    private fun addCustomCategory() {
        print("Category name: ")
        val name = input.nextLine().trim().uppercase()
        if (name.isBlank()) { println("Cancelled."); return }
        val group = promptGroup() ?: return
        registry.addCustom(name, group)
        println("Added $name to session categories.")
    }

    private fun listAllCategories() {
        println("\nBuilt-in categories (${registry.allBuiltIn().size}):")
        registry.allBuiltIn().forEach { println("  %-20s %s".format(it.name, it.group.name)) }
        val custom = registry.allCustom()
        if (custom.isNotEmpty()) {
            println("\nSession-only categories (${custom.size}):")
            custom.forEach { println("  %-20s %s".format(it.name, it.group.name)) }
        }
    }

    // --- Input helpers ---

    private fun promptBuiltInCategory(): BudgetCategory? {
        val categories = registry.allBuiltIn()
        categories.forEachIndexed { i, c -> println("  ${i + 1}. %-20s %s".format(c.name, c.group.name)) }
        val index = readInt("> ", 0, categories.size)
        if (index == 0) return null
        return categories[index - 1]
    }

    private fun promptAmount(): BigDecimal? {
        while (true) {
            print("Amount (USD): ")
            val raw = input.nextLine().trim()
            if (raw.isBlank()) { println("Cancelled."); return null }
            val amount = raw.toBigDecimalOrNull()
            if (amount != null && amount > BigDecimal.ZERO) return amount
            println("Invalid amount. Enter a positive number.")
        }
    }

    private fun promptAmountOrKeep(current: BigDecimal): BigDecimal? {
        print("New amount [\$$current]: ")
        val raw = input.nextLine().trim()
        if (raw.isBlank()) return current
        val amount = raw.toBigDecimalOrNull()
        if (amount != null && amount > BigDecimal.ZERO) return amount
        println("Invalid amount.")
        return null
    }

    private fun promptDate(default: LocalDate): LocalDate {
        print("Date [$default]: ")
        val raw = input.nextLine().trim()
        if (raw.isBlank()) return default
        return try {
            LocalDate.parse(raw, dateFormatter)
        } catch (e: DateTimeParseException) {
            println("Invalid date, using $default.")
            default
        }
    }

    private fun promptDateOrKeep(current: LocalDate): LocalDate {
        print("New date [$current]: ")
        val raw = input.nextLine().trim()
        if (raw.isBlank()) return current
        return try {
            LocalDate.parse(raw, dateFormatter)
        } catch (e: DateTimeParseException) {
            println("Invalid date, keeping $current.")
            current
        }
    }

    private fun promptMonth(): Pair<Int, Int> {
        val now = LocalDate.now()
        val default = now.format(monthFormatter)
        print("Period [$default]: ")
        val raw = input.nextLine().trim()
        if (raw.isBlank()) return Pair(now.year, now.monthValue)
        return try {
            val date = LocalDate.parse("$raw-01", dateFormatter)
            Pair(date.year, date.monthValue)
        } catch (e: DateTimeParseException) {
            println("Invalid period, using current month.")
            Pair(now.year, now.monthValue)
        }
    }

    private fun promptGroup(): BudgetGroup? {
        val groups = BudgetGroup.values()
        groups.forEachIndexed { i, g -> println("  ${i + 1}. ${g.name}") }
        val index = readInt("> ", 0, groups.size)
        if (index == 0) return null
        return groups[index - 1]
    }

    private fun readInt(prompt: String, min: Int, max: Int): Int {
        while (true) {
            print(prompt)
            val raw = input.nextLine().trim()
            val n = raw.toIntOrNull()
            if (n != null && n in min..max) return n
            println("Enter a number between $min and $max.")
        }
    }
}
