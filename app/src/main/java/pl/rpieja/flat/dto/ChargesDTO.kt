package pl.rpieja.flat.dto

import com.google.gson.annotations.SerializedName

data class ChargesDTO(
    @SerializedName("charges")
    var revenues: List<Revenue>,
    var summary: List<Summary>,
    @SerializedName("incomes")
    var expenses: List<Expense>
)
