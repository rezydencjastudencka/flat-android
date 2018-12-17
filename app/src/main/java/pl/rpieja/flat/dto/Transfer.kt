package pl.rpieja.flat.dto

import pl.memleak.flat.TransfersQuery
import java.util.*

data class Transfer(
        var id: String,
        var name: String,
        var date: Date,
        var to: User,
        var from: User,
        var amount: Double
) : ChargeLike {
    constructor(obj: TransfersQuery.Transfer) : this(
            id = obj.id(),
            name = obj.name(),
            date = obj.date(),
            to = User(obj.toUser()),
            from = User(obj.fromUser()),
            amount = obj.amount()
    )

    override val chargeAmount: Double
        get() = amount
    override val chargeName: String
        get() = name
    override val fromUsers: List<User>
        get() = listOf(from)
    override val toUsers: List<User>
        get() = listOf(to)
}
