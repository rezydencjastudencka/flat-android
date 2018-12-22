package pl.rpieja.flat.dto

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import pl.memleak.flat.fragment.RevenueFragment
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
    constructor(obj: RevenueFragment) : this(
            id = obj.id(),
            name = obj.name(),
            date = obj.date(),
            from = User(obj.fromUser().fragments().userFragment()),
            to = obj.toUsers()?.map { User(it.fragments().userFragment()) }.orEmpty(),
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
