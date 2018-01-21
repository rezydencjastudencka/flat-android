package pl.rpieja.flat.dto

data class Summary(
        var name: String,
        var amount: Double,
        var room: Int,
        var id: Int
) : ChargeLike {
    override val chargeAmount: Double
        get() = amount
    override val chargeName: String
        get() = name
    override val fromUsers: List<User>
        get() = emptyList()
    override val toUsers: List<User>
        get() = emptyList()
}
