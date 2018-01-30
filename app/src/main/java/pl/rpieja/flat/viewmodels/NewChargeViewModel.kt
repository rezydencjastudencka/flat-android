package pl.rpieja.flat.viewmodels

import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.Context
import pl.rpieja.flat.api.FlatAPI
import pl.rpieja.flat.authentication.AccountService
import pl.rpieja.flat.authentication.FlatCookieJar
import pl.rpieja.flat.dto.Charge
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
    private var flatApi: FlatAPI? = null


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

        AsyncFetchUsers(getFlatApi(context), { usersList -> users.value = usersList },
                { AccountService.removeCurrentAccount(context) }).execute()
    }

    fun createCharge(context: Context, onSuccess: (Charge) -> Unit) {
        getFlatApi(context)

        val date = IsoTimeFormatter.toIso8601(chargeDate.value?.time ?: Calendar.getInstance().time)
        val to = selectedUsers.value?.map { user -> user.id } ?: emptyList()
        val charge = CreateChargeDTO(chargeName.value!!, date, chargeAmount.value!!, to)

        AsyncCreateCharge(getFlatApi(context), onSuccess,
                { AccountService.removeCurrentAccount(context) }, charge).execute()
    }

    private fun getFlatApi(context: Context): FlatAPI {
        if (flatApi == null) {
            flatApi = FlatAPI(context, FlatCookieJar(context))
        }
        return flatApi!!
    }
}
