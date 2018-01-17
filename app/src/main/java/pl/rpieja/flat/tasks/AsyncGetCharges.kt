package pl.rpieja.flat.tasks

import pl.rpieja.flat.api.FlatAPI
import pl.rpieja.flat.dto.ChargesDTO

class AsyncGetCharges (
        flatAPI: FlatAPI, month: Int, year: Int, onSuccess: (ChargesDTO) -> Unit, unauthorized: () -> Unit)
    : AsyncRequest <ChargesDTO>(
        onSuccess, unauthorized, { flatAPI.fetchCharges(month, year)}, ChargesDTO())
