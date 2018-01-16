package pl.rpieja.flat.fragment

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import pl.rpieja.flat.R
import pl.rpieja.flat.dto.*
import pl.rpieja.flat.viewmodels.TransfersViewModel

abstract class TransferTab : ChargeLayoutFragment<Transfer, TransfersViewModel, TransfersDTO>() {
    override val modelClass: Class<TransfersViewModel> = TransfersViewModel::class.java
    override fun extractLiveData(vm: TransfersViewModel): LiveData<TransfersDTO> = vm.getTransfers()
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
    override fun extractLiveData(vm: TransfersViewModel): LiveData<TransfersDTO> = vm.getTransfers()
    override fun extractEntityFromDTO(dto: TransfersDTO): List<Summary> = dto.summary!!
}

class TransfersFragment : Fragment() {
    companion object {
        val tag = "pl.rpieja.flat.TransfersFragment"
    }

    private var transfersViewModel: TransfersViewModel? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.content_transfers, container, false)

        transfersViewModel = ViewModelProviders.of(activity!!).get(TransfersViewModel::class.java)
        transfersViewModel!!.loadTransfers(context!!, 11, 2017)

        val mSectionsPagerAdapter = object: FragmentPagerAdapter(childFragmentManager) {
            override fun getItem(position: Int): Fragment = when (position) {
                0 -> TransferOutgoingTab()
                1 -> TransferIncomingTab()
                2 -> TransferSummaryTab()
                else -> TODO()
            }

            override fun getCount(): Int {
                return 3
            }
        }

        val viewPager: ViewPager = view.findViewById(R.id.container)
        val tabLayout: TabLayout = view.findViewById(R.id.tabs)

        viewPager.adapter = mSectionsPagerAdapter
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(viewPager))

        return view
    }
}