package pl.rpieja.flat.dto

data class CreateRevenueDTO(
        var name: String,
        var date: String,
        var rawAmount: String,
        var to: List<Int>
) : CreateDTO<Revenue> {
    override val entityClass: Class<Revenue>
        get() = Revenue::class.java
}
