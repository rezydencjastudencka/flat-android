package pl.rpieja.flat.viewmodels

import pl.rpieja.flat.api.FlatAPI
import pl.rpieja.flat.dto.ChargesDTO
import pl.rpieja.flat.dto.Expense
import pl.rpieja.flat.dto.Revenue
import pl.rpieja.flat.dto.Summary
import pl.rpieja.flat.tasks.AsyncGetCharges
import pl.rpieja.flat.tasks.AsyncRequest
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
                              onSuccess: (ChargesDTO) -> Unit,
                              unauthorized: () -> Unit): AsyncRequest<ChargesDTO> =
            AsyncGetCharges(flatAPI, month, year, onSuccess, unauthorized)
}
