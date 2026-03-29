package joe.budget.imports

import joe.budget.categories.BudgetCategory
import joe.budget.categories.CategoryKey
import java.io.File
import java.util.Properties

class CategoryMatcher(rulesFile: File = File("config/category-rules.properties")) {

    private val rules: List<Pair<String, CategoryKey>>

    init {
        val props = Properties()
        if (rulesFile.exists()) {
            rulesFile.inputStream().use { props.load(it) }
        }
        rules = props.entries.map { (key, value) ->
            val keyword = key.toString().lowercase()
            val category = BudgetCategory.values().find { it.name == value.toString().trim().uppercase() }
                ?: BudgetCategory.UNCATEGORIZED
            keyword to CategoryKey.BuiltIn(category)
        }
    }

    fun match(payee: String): CategoryKey {
        val lower = payee.lowercase()
        return rules.firstOrNull { (keyword, _) -> lower.contains(keyword) }?.second
            ?: CategoryKey.BuiltIn(BudgetCategory.UNCATEGORIZED)
    }
}
