package pl.rpieja.flat.viewmodels

import android.arch.lifecycle.MutableLiveData
import android.content.Context

interface Loadable<T> {
    fun load(context: Context)
    val data: MutableLiveData<T>
}

interface MonthlyLoadable<T>: Loadable<T> {
    fun load(context: Context, month: Int, year: Int)
}