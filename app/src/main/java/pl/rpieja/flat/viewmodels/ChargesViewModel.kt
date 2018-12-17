package pl.rpieja.flat.viewmodels

import io.reactivex.Observable
import pl.rpieja.flat.api.FlatAPI
import pl.rpieja.flat.dto.*
import java.util.*

class ChargesViewModel : MonthlyEntityViewModel<ChargesDTO>() {
    fun sortRevenues(comparator: Comparator<Revenue>) {
        if (data.value?.revenues == null) return

        Collections.sort(data.value!!.revenues, comparator)
        data.value = data.value
    }

    fun sortExpenses(comparator: Comparator<Expense>) {
        if (data.value?.expenses == null) return

        Collections.sort(data.value!!.expenses, comparator)
        data.value = data.value
    }

    fun sortSummary(comparator: Comparator<Summary>) {
        if (data.value?.summary == null) return

        Collections.sort(data.value!!.summary, comparator)
        data.value = data.value
    }

    fun addRevenue(revenue: Revenue) {
        if (data.value?.revenues == null) return
        data.value!!.revenues = data.value!!.revenues + revenue
        data.value = data.value
    }

    override fun defaultSort(){
        sortRevenues(Comparator { o1, o2 -> o2.date.compareTo(o1.date) })
    }

    override fun asyncRequest(flatAPI: FlatAPI, month: Int, year: Int,
                              unauthorized: () -> Unit): Observable<ChargesDTO> {
        return flatAPI.fetchCharges(month = month, year = year)
                .filter { !it.hasErrors() }
                .map { it.data()!! }

                .map { ChargesDTO(
                        it.revenues()!!.map { Revenue(
                                it.name(), "", it.date(), it.fromUser().id(),
                                it.id(), it.toUsers()!!.map { User(it.username(), it.id()) },
                                it.amount()
                        ) },
                        it.summary()!!.monthly()!!.map {
                            Summary(
                                it.user()!!.username(), it.amount()!!, 0, 0

                        ) },
                        it.expenses()!!.map { Expense(
                                it.name(), "", it.date(), it.id(), emptyList(),
                                User(it.fromUser().username(), it.fromUser().id()), it.amount()!!
                        ) }
                )}
    }
}
