package pl.rpieja.flat.dto

import pl.memleak.flat.ChargesQuery

data class Summary(
        var user: User,
        var amount: Double
) : ChargeLike {
    constructor(obj: ChargesQuery.Monthly) : this(
            user = User(obj.user()!!),
            amount = obj.amount()!!
    )

    override val chargeAmount: Double
        get() = amount
    override val chargeName: String
        get() = user.name
    override val fromUsers: List<User>
        get() = emptyList()
    override val toUsers: List<User>
        get() = emptyList()
}
