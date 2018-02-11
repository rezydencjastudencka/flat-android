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
    override val data: MutableLiveData<T> = MutableLiveData()
    val date: MutableLiveData<YearMonth> = MutableLiveData()

    init {
        val calendar = Calendar.getInstance()
        val month = calendar.get(Calendar.MONTH) + 1
        val year = calendar.get(Calendar.YEAR)
        date.value = YearMonth(month, year)
    }

    override fun load(context: Context, force: Boolean) {
        return load(context, date.value!!, force)
    }

    override fun load(context: Context, date: YearMonth, force: Boolean) {
        val flatAPI = FlatAPI(context, FlatCookieJar(context))

        // Do not refetch data if date has not been changed
        if (!force && data.value != null && this.date.value == date)
            return

        this.date.value = date

        asyncRequest(flatAPI, date.month, date.year, { data.value = it; defaultSort() },
                { AccountService.removeCurrentAccount(context) }).execute()
    }

    protected abstract fun defaultSort()

    abstract fun asyncRequest(flatAPI: FlatAPI, month: Int, year: Int, onSuccess: (T) -> Unit,
                              unauthorized: () -> Unit): AsyncRequest<T>
}