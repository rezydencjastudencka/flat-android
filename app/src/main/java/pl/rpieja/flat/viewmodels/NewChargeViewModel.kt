package pl.rpieja.flat.viewmodels

import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.Context
import pl.rpieja.flat.api.FlatAPI
import pl.rpieja.flat.authentication.AccountService
import pl.rpieja.flat.authentication.FlatCookieJar
import pl.rpieja.flat.dto.CreateChargeDTO
import pl.rpieja.flat.dto.User
import pl.rpieja.flat.tasks.AsyncCreateCharge
import pl.rpieja.flat.tasks.AsyncFetchUsers
import pl.rpieja.flat.util.IsoTimeFormatter
import java.util.Calendar

class NewChargeViewModel : ViewModel() {

    val users = MutableLiveData<List<User>>()
    val selectedUsers = MutableLiveData<Set<User>>()
    val chargeDate = MutableLiveData<Calendar>()
    val chargeName = MutableLiveData<String>()
    val chargeAmount = MutableLiveData<String>()
    val isValid = MediatorLiveData<Boolean>()
    private var flatAPI : FlatAPI? = null


    init {
        selectedUsers.value = HashSet()
        chargeDate.value = Calendar.getInstance()

        validate()
        isValid.addSource(selectedUsers) { this.validate() }
        isValid.addSource(chargeName) { this.validate() }
        isValid.addSource(chargeAmount) { this.validate() }

    }

    private fun validate() {
        if (selectedUsers.value?.isEmpty() != false){
            isValid.value = false
            return
        }

        if (chargeName.value?.isEmpty() != false){
            isValid.value = false
            return
        }

        if (chargeAmount.value?.isEmpty() != false){
            isValid.value = false
            return
        }

        isValid.value = true
    }

    fun loadUsers(context: Context) {
        if (users.value != null) return

        createFlatApiIfNeeded(context)

        AsyncFetchUsers(flatAPI, { usersList -> users.value = usersList},
                { AccountService.removeCurrentAccount(context) }).execute()
    }

    fun createCharge(context: Context, onSuccess: Runnable) {
        createFlatApiIfNeeded(context)

        val charge = CreateChargeDTO()
        charge.date = IsoTimeFormatter.toIso8601(chargeDate.value?.time ?: Calendar.getInstance().time)
        charge.name = chargeName.value
        charge.rawAmount = chargeAmount.value
        charge.to.addAll(selectedUsers.value?.map { user -> user.id } ?: emptyList())

        AsyncCreateCharge(flatAPI, onSuccess,
                Runnable {AccountService.removeCurrentAccount(context)}).execute(charge)
    }

    private fun createFlatApiIfNeeded(context: Context){
        if (flatAPI == null) {
            flatAPI = FlatAPI(FlatCookieJar(context))
        }
    }
}
