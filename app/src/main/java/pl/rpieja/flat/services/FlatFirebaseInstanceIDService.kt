package pl.rpieja.flat.services

import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService
import pl.rpieja.flat.api.FlatAPI
import pl.rpieja.flat.api.UnauthorizedException


class FlatFirebaseInstanceIDService : FirebaseInstanceIdService() {
    override fun onTokenRefresh() {
        val refreshedToken = FirebaseInstanceId.getInstance().token
        try {
            FlatAPI.getFlatApi(this).registerFCM(refreshedToken!!)
        } catch (e: UnauthorizedException){
            //Do nothing we will register on login
        }
    }
}