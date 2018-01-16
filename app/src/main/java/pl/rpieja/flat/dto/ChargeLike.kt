package pl.rpieja.flat.dto

interface ChargeLike {
    val chargeAmount: Double?
    val chargeName: String?
    val fromUsers: List<User>?
    val toUsers: List<User>?
}