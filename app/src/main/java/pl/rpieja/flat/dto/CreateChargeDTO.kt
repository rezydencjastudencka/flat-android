package pl.rpieja.flat.dto

data class CreateChargeDTO(
        var name: String,
        var date: String,
        var rawAmount: String,
        var to: List<Int>
) : CreateDTO<Charge> {
    override val entityClass: Class<Charge>
        get() = Charge::class.java
}
