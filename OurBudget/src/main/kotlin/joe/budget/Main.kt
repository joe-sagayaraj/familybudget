package joe.budget

import joe.budget.api.Expense
import joe.budget.imports.ImportService
import joe.budget.ui.CategoryRegistry
import joe.budget.ui.ConsoleUI

fun main() {
    val expense = Expense()
    val registry = CategoryRegistry()
    val importService = ImportService(expense)
    val ui = ConsoleUI(expense, registry, importService)
    ui.run()
}
