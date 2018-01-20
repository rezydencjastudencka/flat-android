package pl.rpieja.flat.dto

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.Date

@SuppressLint("ParcelCreator")
@Parcelize
data class Charge(
        var name: String,
        var rawAmount: String,
        var date: Date,
        var from: Int,
        var id: Int,
        var to: List<User>,
        var amount: Double
) : ChargeLike, Parcelable {
    override val chargeName: String
        get() = name
    override val chargeAmount: Double
        get() = amount
    override val fromUsers: List<User>?
        get() = null
    override val toUsers: List<User>?
        get() = to
}
