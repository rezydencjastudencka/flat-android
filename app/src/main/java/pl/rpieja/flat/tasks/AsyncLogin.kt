package pl.rpieja.flat.tasks

import pl.rpieja.flat.api.FlatAPI

class AsyncLogin (
        flatAPI: FlatAPI, username: String, password: String,
        onSuccess: (Unit) -> Unit, unauthorized: () -> Unit)
    : AsyncRequest <Unit>(
        onSuccess, unauthorized, { flatAPI.login(username, password)},
        Unit)
