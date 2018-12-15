package pl.rpieja.flat.fragment

import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import pl.rpieja.flat.R
import pl.rpieja.flat.dto.Summary
import pl.rpieja.flat.dto.Transfer
import pl.rpieja.flat.dto.TransfersDTO
import pl.rpieja.flat.dto.User
import pl.rpieja.flat.viewmodels.TransfersViewModel

abstract class TransferTab : ChargeLayoutFragment<Transfer, TransfersViewModel, TransfersDTO>() {
    override val modelClass: Class<TransfersViewModel> = TransfersViewModel::class.java
    override fun extractLiveData(vm: TransfersViewModel): LiveData<TransfersDTO> = vm.data
}

class TransferOutgoingTab : TransferTab() {
    override fun getUsers(item: Transfer): List<User> = item.toUsers
    override fun extractEntityFromDTO(dto: TransfersDTO): List<Transfer> = dto.outgoing
}

class TransferIncomingTab : TransferTab() {
    override fun getUsers(item: Transfer): List<User> = item.fromUsers
    override fun extractEntityFromDTO(dto: TransfersDTO): List<Transfer> = dto.incoming
}

class TransferSummaryTab : SummaryLayoutFragment<TransfersViewModel, TransfersDTO>() {
    override val modelClass: Class<TransfersViewModel> = TransfersViewModel::class.java
    override fun extractLiveData(vm: TransfersViewModel): LiveData<TransfersDTO> = vm.data
    override fun extractEntityFromDTO(dto: TransfersDTO): List<Summary> = dto.summary
}

class TransfersFragment : EntityMonthlyFragment<TransfersDTO, TransfersViewModel>() {
    companion object {
        const val OUTGOING_TAB_INDEX = 0
        const val INCOMING_TAB_INDEX = 1
        const val SUMMARY_TAB_INDEX = 2
    }

    override val viewModelClass: Class<TransfersViewModel> = TransfersViewModel::class.java
    override val layoutId: Int = R.layout.content_transfers
    override val titleId: Int = R.string.transfers_title
    override val menuId: Int = R.menu.menu_transfers

    override fun getTabFragment(position: Int): Fragment =
            when (position) {
                OUTGOING_TAB_INDEX -> TransferOutgoingTab()
                INCOMING_TAB_INDEX -> TransferIncomingTab()
                SUMMARY_TAB_INDEX -> TransferSummaryTab()
                else -> TODO() // FIXME throw meaningful exception
            }

    override fun getItemCount(): Int = 3

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        super.onOptionsItemSelected(item)
        when (item!!.itemId) {
            R.id.action_setmonth -> showDatePickerDialog()
            else -> super.onOptionsItemSelected(item)
        }
        return true
    }
}
