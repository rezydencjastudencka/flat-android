package pl.rpieja.flat.tasks

import pl.rpieja.flat.api.FlatAPI
import pl.rpieja.flat.dto.Charge
import pl.rpieja.flat.dto.CreateChargeDTO

class AsyncCreateCharge(
        flatAPI: FlatAPI, onSuccess: (Charge) -> Unit,
        unauthorized: () -> Unit, charge: CreateChargeDTO)
    : AsyncRequest<Charge>(
        { result -> onSuccess(result) }, unauthorized, { flatAPI.createCharge(charge) }, null)
