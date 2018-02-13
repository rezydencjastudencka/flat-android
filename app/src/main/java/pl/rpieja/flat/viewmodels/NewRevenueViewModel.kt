package pl.rpieja.flat.viewmodels

import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.Context
import pl.rpieja.flat.api.FlatAPI
import pl.rpieja.flat.authentication.AccountService
import pl.rpieja.flat.authentication.FlatCookieJar
import pl.rpieja.flat.dto.CreateRevenueDTO
import pl.rpieja.flat.dto.Revenue
import pl.rpieja.flat.dto.User
import pl.rpieja.flat.tasks.AsyncCreateRevenue
import pl.rpieja.flat.tasks.AsyncFetchUsers
import pl.rpieja.flat.util.IsoTimeFormatter
import java.util.Calendar
import kotlin.collections.HashSet
import kotlin.collections.List
import kotlin.collections.Set
import kotlin.collections.emptyList
import kotlin.collections.map

class NewRevenueViewModel : ViewModel() {

    val users = MutableLiveData<List<User>>()
    val selectedUsers = MutableLiveData<Set<User>>()
    val date = MutableLiveData<Calendar>()
    val name = MutableLiveData<String>()
    val amount = MutableLiveData<String>()
    val isValid = MediatorLiveData<Boolean>()
    private var flatApi: FlatAPI? = null


    init {
        selectedUsers.value = HashSet()
        date.value = Calendar.getInstance()

        validate()
        isValid.addSource(selectedUsers) { this.validate() }
        isValid.addSource(name) { this.validate() }
        isValid.addSource(amount) { this.validate() }

    }

    private fun validate() {
        if (selectedUsers.value?.isEmpty() != false){
            isValid.value = false
            return
        }

        if (name.value?.isEmpty() != false){
            isValid.value = false
            return
        }

        if (amount.value?.isEmpty() != false){
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

    fun createRevenue(context: Context, onSuccess: (Revenue) -> Unit) {
        getFlatApi(context)

        val date = IsoTimeFormatter.toIso8601(date.value?.time ?: Calendar.getInstance().time)
        val to = selectedUsers.value?.map { user -> user.id } ?: emptyList()
        val charge = CreateRevenueDTO(name.value!!, date, amount.value!!, to)

        AsyncCreateRevenue(getFlatApi(context), onSuccess,
                { AccountService.removeCurrentAccount(context) }, charge).execute()
    }

    private fun getFlatApi(context: Context): FlatAPI {
        if (flatApi == null) {
            flatApi = FlatAPI(context, FlatCookieJar(context))
        }
        return flatApi!!
    }
}
