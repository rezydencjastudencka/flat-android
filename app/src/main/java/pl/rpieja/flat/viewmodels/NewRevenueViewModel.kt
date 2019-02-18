package pl.rpieja.flat.viewmodels

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import pl.rpieja.flat.R
import pl.rpieja.flat.api.FlatAPI
import pl.rpieja.flat.dto.Category
import pl.rpieja.flat.dto.CreateRevenueDTO
import pl.rpieja.flat.dto.Revenue
import pl.rpieja.flat.dto.User
import java.util.Calendar
import kotlin.collections.HashSet

class NewRevenueViewModel : ViewModel() {

    val categories = MutableLiveData<List<Category>>()
    val selectedCategory = MutableLiveData<Category>()
    val users = MutableLiveData<List<User>>()
    val selectedUsers = MutableLiveData<Set<User>>()
    val date = MutableLiveData<Calendar>()
    val name = MutableLiveData<String>()
    val amount = MutableLiveData<String>()
    val isValid = MediatorLiveData<Boolean>()

    private var requests = CompositeDisposable()


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
        if (selectedCategory.value === null) {
            isValid.value = false
            return
        }

        isValid.value = true
    }

    fun loadUsers(context: Context) {
        if (users.value != null) return

        this.requests.add(FlatAPI.getFlatApi(context).fetchUsers()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { users.value = it },
                        { Toast.makeText(context, R.string.network_error, Toast.LENGTH_LONG).show() }
                ))
        this.requests.add(FlatAPI.getFlatApi(context).fetchCategories()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { categories.value = it },
                        { Toast.makeText(context, R.string.network_error, Toast.LENGTH_LONG).show() }
                ))
    }

    fun createRevenue(context: Context, onSuccess: (Revenue) -> Unit) {

        val date = date.value?.time ?: Calendar.getInstance().time
        val to = selectedUsers.value?.map { user -> user.id } ?: emptyList()
        val category = selectedCategory.value!!.id
        val charge = CreateRevenueDTO(name.value!!, date, amount.value!!, category, to)

        this.requests.add(FlatAPI.getFlatApi(context).createRevenue(charge)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { onSuccess(it) },
                        { Toast.makeText(context, R.string.network_error, Toast.LENGTH_LONG).show() }
                ))
    }
}
