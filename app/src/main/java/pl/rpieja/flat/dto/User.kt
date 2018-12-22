package pl.rpieja.flat.dto

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import pl.memleak.flat.fragment.UserFragment

@Parcelize
data class User(var name: String, var id: String) : Parcelable {
    constructor(obj: UserFragment) : this(obj.username(), obj.id())
}
