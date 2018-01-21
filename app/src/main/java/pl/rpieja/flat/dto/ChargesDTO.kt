package pl.rpieja.flat.dto

data class ChargesDTO(
        var charges: List<Charge>,
        var summary: List<Summary>,
        var incomes: List<Expense>
)
