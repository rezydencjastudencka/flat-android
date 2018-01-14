package pl.rpieja.flat.dto

import java.util.*

/**
 * Created by radix on 13.01.18.
 */
class Transfer {
    var name: String? = null
    var date: Date? = null
    var id: Int = 0
    var to: List<User>? = null
    var from: User? = null
    var amount: Double = 0.toDouble()
}
