package pl.rpieja.flat.dto

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Revenue(
        var name: String,
        var rawAmount: String,
        var date: Date,
        var from: String,
        var id: String,
        var to: List<User>,
        var amount: Double
) : ChargeLike, Parcelable {
    override val chargeName: String
        get() = name
    override val chargeAmount: Double
        get() = amount
    override val fromUsers: List<User>
        get() = emptyList()
    override val toUsers: List<User>
        get() = to
}
