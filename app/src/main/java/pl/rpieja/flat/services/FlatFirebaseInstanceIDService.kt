package pl.rpieja.flat.services

import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService
import pl.rpieja.flat.api.FlatAPI


class FlatFirebaseInstanceIDService : FirebaseInstanceIdService() {
    override fun onTokenRefresh() {
        val refreshedToken = FirebaseInstanceId.getInstance().token
        FlatAPI.getFlatApi(this).registerFCM(refreshedToken!!)
    }
}