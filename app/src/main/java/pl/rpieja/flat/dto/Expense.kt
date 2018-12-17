package pl.rpieja.flat.dto

import pl.memleak.flat.ChargesQuery
import java.util.*

data class Expense(
        var id: String,
        var name: String,
        var date: Date,
        var to: List<User>,
        var from: User,
        var amount: Double
) : ChargeLike {
    constructor(obj: ChargesQuery.Expense) : this(
            id = obj.id(),
            name = obj.name(),
            date = obj.date(),
            to = obj.toUsers()?.map { User(it) }.orEmpty(),
            from = User(obj.fromUser()),
            amount = obj.amount()!!
    )

    override val chargeAmount: Double
        get() = amount
    override val chargeName: String
        get() = name
    override val fromUsers: List<User>
        get() = listOf(from)
    override val toUsers: List<User>
        get() = to
}
