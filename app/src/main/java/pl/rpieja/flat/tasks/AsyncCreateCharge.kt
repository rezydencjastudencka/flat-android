package pl.rpieja.flat.tasks

import pl.rpieja.flat.api.FlatAPI
import pl.rpieja.flat.dto.CreateChargeDTO

class AsyncCreateCharge(
        flatAPI: FlatAPI, onSuccess: () -> Unit,
        unauthorized: () -> Unit, charge: CreateChargeDTO)
    : AsyncRequest<Unit>(
        { onSuccess() }, unauthorized, { flatAPI.createCharge(charge) }, Unit)
