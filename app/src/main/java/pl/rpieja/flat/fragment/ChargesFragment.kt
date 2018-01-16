package pl.rpieja.flat.fragment

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.view.*
import com.rackspira.kristiawan.rackmonthpicker.RackMonthPicker
import pl.rpieja.flat.R
import pl.rpieja.flat.dialog.ChargesSortDialogFragment
import pl.rpieja.flat.dto.*
import pl.rpieja.flat.viewmodels.ChargesViewModel
import java.util.*

abstract class ChargeTab<T: ChargeLike> : ChargeLayoutFragment<T, ChargesViewModel, ChargesDTO>() {
    override val modelClass: Class<ChargesViewModel> = ChargesViewModel::class.java
    override fun extractLiveData(vm: ChargesViewModel): LiveData<ChargesDTO> = vm.charges
    override fun createViewHolder(view: View): ChargeViewHolder = ChargeViewHolder(view)
}

class ChargeIncomeTab : ChargeTab<Charge>() {
    override fun getUsers(item: Charge): List<User> = item.toUsers!! // FIXME remove !!
    override fun extractEntityFromDTO(dto: ChargesDTO): List<Charge> = dto.charges
}

class ChargeExpenseTab : ChargeTab<Expense>() {
    override fun getUsers(item: Expense): List<User> = item.fromUsers!! // FIXME remove !!
    override fun extractEntityFromDTO(dto: ChargesDTO): List<Expense> = dto.incomes
}

class ChargeSummaryTab : SummaryLayoutFragment<ChargesViewModel, ChargesDTO>() {
    override val modelClass: Class<ChargesViewModel> = ChargesViewModel::class.java
    override fun extractLiveData(vm: ChargesViewModel): LiveData<ChargesDTO> = vm.charges
    override fun extractEntityFromDTO(dto: ChargesDTO): List<Summary> = dto.summary
}

class SectionsPagerAdapter(fm: FragmentManager): FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment =
            when (position) {
                0 -> ChargeIncomeTab()
                1 -> ChargeExpenseTab()
                2 -> ChargeSummaryTab()
                else -> TODO()
            }

    override fun getCount(): Int = 3
}

class ChargesFragment: Fragment() {
    companion object {
        val tag = "pl.rpieja.flat.fragment.ChargesFragment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    private fun showDatePickerDialog() =
            RackMonthPicker(context)
                    .setLocale(Locale.ENGLISH)
                    .setColorTheme(R.color.colorPrimaryDark)
                    .setSelectedMonth(chargesViewModel!!.month - 1)
                    .setPositiveButton({ month, _, _, year, _ ->
                        chargesViewModel!!.loadCharges(context, month, year) })
                    .setNegativeButton({ picker -> picker.dismiss() })
                    .show()

    private var chargesViewModel: ChargesViewModel? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.content_charges, container, false)

        chargesViewModel = ViewModelProviders.of(activity!!).get(ChargesViewModel::class.java)
        chargesViewModel!!.loadCharges(context)

        val mSectionsPagerAdapter = SectionsPagerAdapter(childFragmentManager)

        val viewPager: ViewPager = view.findViewById(R.id.container)
        val tabLayout: TabLayout = view.findViewById(R.id.tabs)

        viewPager.adapter = mSectionsPagerAdapter
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(viewPager))

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater!!.inflate(R.menu.menu_charges, menu!!)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        super.onOptionsItemSelected(item)
        when (item!!.itemId) {
            R.id.action_sort_by ->
                ChargesSortDialogFragment().show(fragmentManager, ChargesSortDialogFragment.tag)

            R.id.action_setmonth ->
                showDatePickerDialog()

            else ->
                super.onOptionsItemSelected(item)
        }
        return true
    }
}