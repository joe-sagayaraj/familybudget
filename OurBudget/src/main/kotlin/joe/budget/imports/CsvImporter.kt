package joe.budget.imports

import java.io.File
import java.math.BigDecimal
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class CsvImporter {

    private val dateFormatters = listOf(
        DateTimeFormatter.ofPattern("MM/dd/yyyy"),
        DateTimeFormatter.ofPattern("M/d/yyyy"),
        DateTimeFormatter.ofPattern("yyyy-MM-dd"),
        DateTimeFormatter.ofPattern("dd/MM/yyyy")
    )

    fun parse(file: File): ParsedCsv {
        val rows = mutableListOf<ParsedRow>()
        val errors = mutableListOf<String>()

        val lines = file.readLines()
        if (lines.size <= 1) return ParsedCsv(emptyList(), emptyList())

        lines.drop(1).forEachIndexed { i, line ->
            val lineNum = i + 2
            if (line.isBlank()) return@forEachIndexed

            val parts = line.split(",").map { it.trim().removeSurrounding("\"") }
            if (parts.size < 3) {
                errors.add("Line $lineNum: skipped — too few columns")
                return@forEachIndexed
            }

            val date = parseDate(parts[0]) ?: run {
                errors.add("Line $lineNum: skipped — invalid date '${parts[0]}'")
                return@forEachIndexed
            }
            val payee = parts[1]
            val amount = parts[2].replace("$", "").replace(",", "").toBigDecimalOrNull()?.abs() ?: run {
                errors.add("Line $lineNum: skipped — invalid amount '${parts[2]}'")
                return@forEachIndexed
            }

            rows.add(ParsedRow(date, payee, amount))
        }

        return ParsedCsv(rows, errors)
    }

    private fun parseDate(raw: String): LocalDate? {
        for (fmt in dateFormatters) {
            try { return LocalDate.parse(raw.trim(), fmt) } catch (_: DateTimeParseException) {}
        }
        return null
    }
}

data class ParsedRow(val date: LocalDate, val payee: String, val amount: BigDecimal)
data class ParsedCsv(val rows: List<ParsedRow>, val errors: List<String>)
