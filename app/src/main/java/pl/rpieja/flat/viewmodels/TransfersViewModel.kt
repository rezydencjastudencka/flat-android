package pl.rpieja.flat.viewmodels

import pl.rpieja.flat.api.FlatAPI
import pl.rpieja.flat.dto.TransfersDTO
import pl.rpieja.flat.tasks.AsyncGetTransfers
import pl.rpieja.flat.tasks.AsyncRequest

class TransfersViewModel : MonthlyEntityViewModel<TransfersDTO>() {
    override fun asyncRequest(flatAPI: FlatAPI, month: Int, year: Int,
                              onSuccess: (TransfersDTO) -> Unit, unauthorized: () -> Unit):
            AsyncRequest<TransfersDTO> =
            AsyncGetTransfers(flatAPI, month, year, onSuccess, unauthorized)
}