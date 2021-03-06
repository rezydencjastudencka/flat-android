package pl.rpieja.flat.viewmodels

import io.reactivex.Observable
import pl.rpieja.flat.api.FlatAPI
import pl.rpieja.flat.dto.TransfersDTO

class TransfersViewModel : MonthlyEntityViewModel<TransfersDTO>() {
    override fun defaultSort() {
    }

    override fun asyncRequest(flatAPI: FlatAPI, month: Int, year: Int)
            : Observable<TransfersDTO> = flatAPI.fetchTransfers(month, year)
}