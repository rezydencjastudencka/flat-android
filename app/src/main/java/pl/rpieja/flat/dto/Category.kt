package pl.rpieja.flat.dto

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import pl.memleak.flat.fragment.CategoryFragment

@Parcelize
data class Category (
        var id: String,
        var name: String) : Parcelable {
    constructor(fragment: CategoryFragment) : this(
            id = fragment.id(),
            name = fragment.name()
    )
}