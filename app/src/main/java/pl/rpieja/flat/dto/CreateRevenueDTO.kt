package pl.rpieja.flat.dto

import java.util.*

data class CreateRevenueDTO(
        var name: String,
        var date: Date,
        var rawAmount: String,
        var to: List<String>
)