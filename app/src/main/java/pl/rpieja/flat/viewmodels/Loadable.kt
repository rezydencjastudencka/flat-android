package pl.rpieja.flat.viewmodels

import android.annotation.SuppressLint
import android.content.Context
import android.os.Parcelable
import androidx.lifecycle.MutableLiveData
import kotlinx.android.parcel.Parcelize

interface Loadable<T> {
    fun load(context: Context, force: Boolean = false)
    val data: MutableLiveData<T>
}

/**
 * Represents month and year pair.
 *
 * NOTE: There is java.time.YearMonth implementation provided by Java 8, but it is available since
 * API 26.
 */
@SuppressLint("ParcelCreator")
@Parcelize
data class YearMonth(val month: Int, val year: Int) : Parcelable

interface MonthlyLoadable<T>: Loadable<T> {
    fun load(context: Context, date: YearMonth, force: Boolean = false)
}