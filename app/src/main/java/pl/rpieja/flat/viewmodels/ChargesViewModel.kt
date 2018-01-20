package pl.rpieja.flat.viewmodels

import java.util.Collections
import java.util.Comparator
import pl.rpieja.flat.api.FlatAPI
import pl.rpieja.flat.dto.Charge
import pl.rpieja.flat.dto.ChargesDTO
import pl.rpieja.flat.dto.Expense
import pl.rpieja.flat.dto.Summary
import pl.rpieja.flat.tasks.AsyncGetCharges
import pl.rpieja.flat.tasks.AsyncRequest

class ChargesViewModel : MonthlyEntityViewModel<ChargesDTO>() {
    fun sortCharges(comparator: Comparator<Charge>) {
        if (data.value?.charges == null) return

        Collections.sort(data.value!!.charges, comparator)
        data.value = data.value
    }

    fun sortIncomes(comparator: Comparator<Expense>) {
        if (data.value?.incomes == null) return

        Collections.sort(data.value!!.incomes, comparator)
        data.value = data.value
    }

    fun sortSummary(comparator: Comparator<Summary>) {
        if (data.value?.summary == null) return

        Collections.sort(data.value!!.summary, comparator)
        data.value = data.value
    }

    fun addCharge(charge: Charge) {
        if (data.value?.charges == null) return
        data.value!!.charges.add(charge)
        data.value = data.value
    }

    override fun asyncRequest(flatAPI: FlatAPI, month: Int, year: Int,
                              onSuccess: (ChargesDTO) -> Unit,
                              unauthorized: () -> Unit): AsyncRequest<ChargesDTO> =
            AsyncGetCharges(flatAPI, month, year, onSuccess, unauthorized)
}
