package pl.rpieja.flat.viewmodels

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.Context
import pl.rpieja.flat.api.FlatAPI
import pl.rpieja.flat.authentication.AccountService
import pl.rpieja.flat.authentication.FlatCookieJar
import pl.rpieja.flat.tasks.AsyncRequest
import java.util.*

abstract class MonthlyEntityViewModel<T>: MonthlyLoadable<T>, ViewModel() {
    var month: Int
    var year: Int

    override val data: MutableLiveData<T> = MutableLiveData()

    init {
        val calendar = Calendar.getInstance()
        month = calendar.get(Calendar.MONTH) + 1
        year = calendar.get(Calendar.YEAR)
    }

    override fun load(context: Context) {
        return load(context, month, year)
    }

    override fun load(context: Context, month: Int, year: Int) {
        val flatAPI = FlatAPI(FlatCookieJar(context))

        // Do not refetch data if month/year are the same
        if (data.value != null && month == this.month && year == this.year)
            return

        this.month = month
        this.year = year

        asyncRequest(flatAPI, month, year, { data.value = it; defaultSort() },
                { AccountService.removeCurrentAccount(context) }).execute()
    }

    protected abstract fun defaultSort()

    abstract fun asyncRequest(flatAPI: FlatAPI, month: Int, year: Int, onSuccess: (T) -> Unit,
                              unauthorized: () -> Unit): AsyncRequest<T>
}