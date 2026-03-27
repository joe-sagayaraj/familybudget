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

## Roadmap

### Goals
- Shared family budget tracker (one budget for the whole family)
- CLI first, browser (REST API) later for family members
- USD only for now
- No persistence yet — in-memory only; SQLite planned for a future phase

### Phase 1 — Clean up & Tests (next up)
- Fix `ExpenseService` companion object structure
- Implement `remove()` and `update()`
- Add `getByCategory()`, `getAll()`, `getMonthlySummary()`
- Write JUnit 5 tests

### Phase 2 — CLI
- Menu loop in Kotlin replacing `Main.java`
- Add expense: pick from existing categories or add a custom one at runtime; amount; date defaults to today
- View expenses: by category or monthly summary; default to current month with option to change period; show both individual entries and totals
- Modify expense: update or delete an entry
- Manage categories: add custom category at runtime (not persisted until persistence layer is built)

### Phase 3 — Browser-ready foundation
- Repository pattern to separate business logic from storage
- `ConsoleUI` kept separate from business logic so a REST layer can be added later with minimal rework
- When adding an expense, allow selecting from both built-in and session-only custom categories (currently only built-in categories are shown)

