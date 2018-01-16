package pl.rpieja.flat.fragment

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
import pl.rpieja.flat.viewmodels.TransfersViewModel

class TransfersFragment: Fragment() {
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
                0 -> TransfersOutgoingTab()
                1 -> TransfersIncomingTab()
                2 -> TransfersSummaryTab()
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