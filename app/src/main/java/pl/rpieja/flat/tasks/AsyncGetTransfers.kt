package pl.rpieja.flat.tasks

import pl.rpieja.flat.api.FlatAPI
import pl.rpieja.flat.dto.TransfersDTO


class AsyncGetTransfers (
        flatAPI: FlatAPI, month: Int, year: Int, onSuccess: (TransfersDTO) -> Unit, unauthorized: () -> Unit)
    : AsyncRequest <TransfersDTO>(
        onSuccess, unauthorized, { flatAPI.getTransfers(month, year)}, TransfersDTO())