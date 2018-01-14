package pl.rpieja.flat

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.view.*
import com.rackspira.kristiawan.rackmonthpicker.RackMonthPicker
import pl.rpieja.flat.viewmodels.ChargesViewModel
import java.util.*

class SectionsPagerAdapter(fm: FragmentManager): FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment =
            when (position) {
                0 -> ChargesTab()
                1 -> ExpensesTab()
                2 -> SummaryTab()
                else -> TODO()
            }

    override fun getCount(): Int = 3
}

class ChargesFragment: Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    fun showDatePickerDialog() {
        val picker = RackMonthPicker(context);
        picker.setLocale(Locale.ENGLISH)
                .setColorTheme(R.color.colorPrimaryDark)
                .setPositiveButton({ month, startDate, endDate, year, monthLabel ->
//            ac.month = month;
//            ac.year = year;
            chargesViewModel!!.loadCharges(context, month, year);
        })
        .setNegativeButton({ _ -> picker.dismiss() }).show();
    }

    private var chargesViewModel: ChargesViewModel? = null
    private var viewPager: ViewPager? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.content_charges, container, false)

        chargesViewModel = ViewModelProviders.of(activity!!).get(ChargesViewModel::class.java)
        chargesViewModel!!.loadCharges(context, 1, 2018)

        val mSectionsPagerAdapter = SectionsPagerAdapter(fragmentManager!!)

        viewPager = view.findViewById(R.id.container)
        val tabLayout: TabLayout = view.findViewById(R.id.tabs)

        viewPager!!.adapter = mSectionsPagerAdapter
        viewPager!!.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(viewPager))

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater!!.inflate(R.menu.menu_charges, menu!!)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.action_sort_by -> ChargesSortDialogFragment()
                    .show(fragmentManager, "ChargesDialog")

            R.id.action_setmonth -> showDatePickerDialog()

            else -> super.onOptionsItemSelected(item)
        }

        return true
    }
}