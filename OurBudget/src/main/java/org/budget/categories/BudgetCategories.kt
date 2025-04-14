package org.budget.categories

enum class BudgetCategories(val group: BudgetGroup, val frequency: BudgetFrequency=BudgetFrequency.MONTHLY) {
    GROCERIES(BudgetGroup.FOOD),
    PERSONAL_CARE(BudgetGroup.WELLNESS),
    PIANO_LESSONS(BudgetGroup.EDUCATION),
    GAS(BudgetGroup.AUTO),
    AUTO_INSURANCE(BudgetGroup.AUTO),
    MORTGAGE(BudgetGroup.HOUSING),
    HOME_INSURANCE(BudgetGroup.HOUSING),
    COUNTY_TAX(BudgetGroup.HOUSING),
    CHURCH_DONATIONS(BudgetGroup.DONATIONS, BudgetFrequency.WEEKLY),
    NETFLIX(BudgetGroup.SUBSCRIPTION),
    PRIME(BudgetGroup.SUBSCRIPTION),
    HULU(BudgetGroup.SUBSCRIPTION),
    SPOTIFY(BudgetGroup.SUBSCRIPTION),
    KINDLE(BudgetGroup.SUBSCRIPTION),
    DINING(BudgetGroup.ENTERTAINMENT),
    INTERNET(BudgetGroup.UTILITIES),
    PSE(BudgetGroup.UTILITIES),
    PUD(BudgetGroup.UTILITIES),
    HOA(BudgetGroup.UTILITIES),
    HOUSE_CLEANING(BudgetGroup.UTILITIES, BudgetFrequency.BIMONTHLY),
    PEST_CONTROL(BudgetGroup.UTILITIES, BudgetFrequency.QUARTERLY),
    SEWER(BudgetGroup.UTILITIES),
    WATER(BudgetGroup.UTILITIES, BudgetFrequency.BIMONTHLY)
}