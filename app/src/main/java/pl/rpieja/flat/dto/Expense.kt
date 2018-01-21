package pl.rpieja.flat.dto

import java.util.*

data class Expense(
        var name: String,
        var rawAmount: String,
        var date: Date,
        var id: Int,
        var to: List<User>,
        var from: User,
        var amount: Double
) : ChargeLike {
    override val chargeAmount: Double
        get() = amount
    override val chargeName: String
        get() = name
    override val fromUsers: List<User>
        get() = listOf(from)
    override val toUsers: List<User>
        get() = to
}
