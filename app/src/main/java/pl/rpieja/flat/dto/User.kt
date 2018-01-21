package pl.rpieja.flat.dto

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator") // FIXME maybe caused by Kotlin Android ticket KT-19300
@Parcelize
data class User(var name: String, var id: Int) : Parcelable
