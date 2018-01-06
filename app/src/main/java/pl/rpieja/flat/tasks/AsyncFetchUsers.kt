package pl.rpieja.flat.tasks

import pl.rpieja.flat.api.FlatAPI
import pl.rpieja.flat.dto.User


class AsyncFetchUsers(
        flatAPI: FlatAPI, onSuccess: (List<User>) -> Unit, unauthorized: () -> Unit)
    : AsyncRequest<List<User>>(
        onSuccess, unauthorized, flatAPI::fetchUsers, emptyList())
