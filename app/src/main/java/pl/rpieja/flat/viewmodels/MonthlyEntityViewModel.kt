package pl.rpieja.flat.viewmodels

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import pl.rpieja.flat.api.FlatAPI
import pl.rpieja.flat.authentication.AccountService
import pl.rpieja.flat.authentication.FlatCookieJar
import java.util.*


abstract class MonthlyEntityViewModel<T>: MonthlyLoadable<T>, ViewModel() {
    override val data: MutableLiveData<T> = MutableLiveData()
    val date: MutableLiveData<YearMonth> = MutableLiveData()
    private var request: Disposable? = null

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

        this.request = asyncRequest(flatAPI, date.month, date.year) { AccountService
                .removeCurrentAccount(context) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { data.value = it }
    }

    protected abstract fun defaultSort()

    abstract fun asyncRequest(flatAPI: FlatAPI, month: Int, year: Int,
                              unauthorized: () -> Unit): Observable<T>
}