package pl.rpieja.flat.dto

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(var name: String, var id: String) : Parcelable
