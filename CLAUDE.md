# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Run Commands

All commands run from the `OurBudget/` directory:

```bash
./gradlew build       # Compile and build
./gradlew test        # Run all tests
./gradlew run         # Run the application
./gradlew clean       # Clean build artifacts
```

To run a single test class:
```bash
./gradlew test --tests "fully.qualified.TestClassName"
```

## Architecture

This is a Kotlin/Java family budget application (JVM 15, Kotlin 1.9.23, JUnit 5).

**Package:** `joe.budget` under `src/main/kotlin/`

**Layered structure:**

- **`api/Expense.kt`** — Public-facing API; thin wrapper that delegates to the service layer. Entry point for callers.
- **`service/ExpenseService.kt`** — Business logic. Holds a companion object with a static `MutableMap<BudgetCategory, MutableList<ExpenseData>>` as in-memory storage.
- **`data/ExpenseData.kt`** — Data class: `date: LocalDate`, `price: BigDecimal`, `currency: String`.
- **`categories/`** — Domain enums:
  - `BudgetCategory` — 26 named expense categories (e.g., GROCERIES, MORTGAGE, NETFLIX), each tagged with a `BudgetGroup` and optional `BudgetFrequency`.
  - `BudgetGroup` — 11 top-level groupings (FOOD, AUTO, HOUSING, UTILITIES, etc.).
  - `BudgetFrequency` — Recurrence types (DAILY, WEEKLY, MONTHLY, BIMONTHLY, QUARTERLY, etc.).

**Entry point:** `src/main/java/Main.java` — creates an `Expense` and exercises the API.

`TestLearn.kt` in the main source tree is a scratch/learning file, not a test.
