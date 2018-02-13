package pl.rpieja.flat.tasks

import pl.rpieja.flat.api.FlatAPI
import pl.rpieja.flat.dto.CreateRevenueDTO
import pl.rpieja.flat.dto.Revenue

class AsyncCreateRevenue(
        flatAPI: FlatAPI, onSuccess: (Revenue) -> Unit,
        unauthorized: () -> Unit, revenue: CreateRevenueDTO)
    : AsyncRequest<Revenue>(
        { result -> onSuccess(result) }, unauthorized, { flatAPI.createRevenue(revenue) }, null)
