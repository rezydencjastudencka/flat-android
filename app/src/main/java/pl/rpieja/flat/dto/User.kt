package pl.rpieja.flat.dto

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import pl.memleak.flat.ChargesQuery
import pl.memleak.flat.TransfersQuery

@Parcelize
data class User(var name: String, var id: String) : Parcelable {
    constructor(obj: ChargesQuery.FromUser) : this(obj.username(), obj.id())
    constructor(obj: ChargesQuery.ToUser) : this(obj.username(), obj.id())
    constructor(obj: ChargesQuery.ToUser1) : this(obj.username(), obj.id())
    constructor(obj: ChargesQuery.FromUser1) : this(obj.username(), obj.id())
    constructor(obj: ChargesQuery.User) : this(obj.username(), obj.id())
    constructor(obj: TransfersQuery.User) : this(obj.username(), obj.id())
    constructor(obj: TransfersQuery.ToUser) : this(obj.username(), obj.id())
    constructor(obj: TransfersQuery.FromUser) : this(obj.username(), obj.id())
}
