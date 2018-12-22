package pl.rpieja.flat.dto

import pl.memleak.flat.ChargesQuery

data class ChargesDTO(
        var revenues: List<Revenue>,
        var expenses: List<Expense>,
        var summary: List<Summary>
) {
    constructor(obj: ChargesQuery.Data) : this(
            revenues = obj.revenues()!!.map { Revenue(it.fragments().revenueFragment()) },
            expenses = obj.expenses()!!.map { Expense(it.fragments().expenseFragment()) },
            summary = obj.summary()!!.monthly()!!.map { Summary(it) }
    )
}
