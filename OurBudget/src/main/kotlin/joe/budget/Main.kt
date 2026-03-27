package joe.budget

import joe.budget.api.Expense
import joe.budget.ui.CategoryRegistry
import joe.budget.ui.ConsoleUI

fun main() {
    val expense = Expense()
    val registry = CategoryRegistry()
    val ui = ConsoleUI(expense, registry)
    ui.run()
}
