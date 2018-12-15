package pl.rpieja.flat.tasks

import pl.rpieja.flat.api.FlatAPI

class AsyncLogin (
        flatAPI: FlatAPI, username: String, password: String,
        registration_token: String?,
        onSuccess: (Boolean) -> Unit, unauthorized: () -> Unit)
    : AsyncRequest<Boolean>(
        onSuccess, unauthorized,
        {
            val result = flatAPI.login(username, password)
            if(registration_token != null) {
                flatAPI.registerFCM(registration_token)
            }
            result
        },
        false)
