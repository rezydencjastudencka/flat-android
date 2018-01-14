package pl.rpieja.flat.viewmodels

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.Context
import pl.rpieja.flat.api.FlatAPI
import pl.rpieja.flat.authentication.AccountService
import pl.rpieja.flat.authentication.FlatCookieJar
import pl.rpieja.flat.dto.*
import pl.rpieja.flat.tasks.AsyncGetTransfers
import java.util.*

/**
 * Created by radix on 14.01.18.
 */

class TransfersViewModel : ViewModel() {
    private val transfers = MutableLiveData<TransfersDTO>()
    private var month: Int? = null
    private var year: Int? = null

    fun getTransfers(): MutableLiveData<TransfersDTO> {
        return transfers
    }

    fun getChargesList(): MutableLiveData<TransfersDTO> {
        return transfers
    }

    fun getIncomesList(): List<Transfer> {
        return if (transfers.value == null) ArrayList() else transfers.value!!.incoming!!
    }

    fun loadTransfers(context: Context, month: Int, year: Int) {
        val flatAPI = FlatAPI(FlatCookieJar(context))

        // Do not refetch data if month/year are the same
        if (this.month != null && this.year != null && month == this.month && year == this.year)
            return

        this.month = month
        this.year = year

        AsyncGetTransfers(flatAPI, month, year,
                { transfersDTO -> transfers.setValue(transfersDTO) }
        ) { AccountService.removeCurrentAccount(context) }.execute()
    }
}