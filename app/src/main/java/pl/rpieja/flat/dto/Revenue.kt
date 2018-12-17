package pl.rpieja.flat.dto

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import pl.memleak.flat.ChargesQuery
import java.util.*

@Parcelize
data class Revenue(
        var id: String,
        var name: String,
        var date: Date,
        var from: User,
        var to: List<User>,
        var amount: Double
) : ChargeLike, Parcelable {
    constructor(obj: ChargesQuery.Revenue) : this(
            id  = obj.id(),
            name = obj.name(),
            date = obj.date(),
            from = User(obj.fromUser()),
            to = obj.toUsers()?.map { User(it) }.orEmpty(),
            amount = obj.amount()
    )

    override val chargeName: String
        get() = name
    override val chargeAmount: Double
        get() = amount
    override val fromUsers: List<User>
        get() = listOf(from)
    override val toUsers: List<User>
        get() = to
}
