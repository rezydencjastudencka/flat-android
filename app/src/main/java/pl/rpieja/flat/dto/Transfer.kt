package pl.rpieja.flat.dto

import java.util.*

/**
 * Created by radix on 13.01.18.
 */
class Transfer: ChargeLike {
    var name: String? = null
    var date: Date? = null
    var id: Int = 0
    var to: User? = null
    var from: User? = null
    var amount: Double? = null

    override val chargeAmount: Double? = amount
    override val chargeName: String? = name
    override val fromUsers: List<User>? = listOf(from!!)
    override val toUsers: List<User>? = listOf(to!!)
}
