package pl.rpieja.flat.dto

import java.util.*

data class Transfer(
        var name: String,
        var date: Date,
        var id: Int,
        var to: User,
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
        get() = listOf(to)
}
