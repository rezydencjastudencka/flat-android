package pl.rpieja.flat.fragment

import android.arch.lifecycle.LiveData
import android.support.v4.app.Fragment
import android.view.MenuItem
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
    override fun getUsers(item: Transfer): List<User> = item.toUsers!!
    override fun extractEntityFromDTO(dto: TransfersDTO): List<Transfer> = dto.outgoing!!
}

class TransferIncomingTab : TransferTab() {
    override fun getUsers(item: Transfer): List<User> = item.fromUsers!!
    override fun extractEntityFromDTO(dto: TransfersDTO): List<Transfer> = dto.incoming!!
}

class TransferSummaryTab : SummaryLayoutFragment<TransfersViewModel, TransfersDTO>() {
    override val modelClass: Class<TransfersViewModel> = TransfersViewModel::class.java
    override fun extractLiveData(vm: TransfersViewModel): LiveData<TransfersDTO> = vm.data
    override fun extractEntityFromDTO(dto: TransfersDTO): List<Summary> = dto.summary!!
}

class TransfersFragment : EntityMonthlyFragment<TransfersDTO, TransfersViewModel>() {
    override val viewModelClass: Class<TransfersViewModel> = TransfersViewModel::class.java
    override val layoutId: Int = R.layout.content_transfers
    override val titleId: Int = R.string.transfers_name
    override val menuId: Int = R.menu.menu_transfers

    override fun getTabFragment(position: Int): Fragment =
            when (position) {
                0 -> TransferOutgoingTab()
                1 -> TransferIncomingTab()
                2 -> TransferSummaryTab()
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
