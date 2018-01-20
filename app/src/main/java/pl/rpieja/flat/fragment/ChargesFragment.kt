package pl.rpieja.flat.fragment

import android.app.Activity.RESULT_OK
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import pl.rpieja.flat.R
import pl.rpieja.flat.activity.NewChargeActivity
import pl.rpieja.flat.activity.NewChargeActivity.Constants.REQUEST_CREATE
import pl.rpieja.flat.activity.NewChargeActivity.Constants.RESULT_CREATE
import pl.rpieja.flat.dialog.ChargesSortDialogFragment
import pl.rpieja.flat.dto.*
import pl.rpieja.flat.viewmodels.ChargesViewModel

abstract class ChargeTab<T: ChargeLike> : ChargeLayoutFragment<T, ChargesViewModel, ChargesDTO>() {
    override val modelClass: Class<ChargesViewModel> = ChargesViewModel::class.java
    override fun extractLiveData(vm: ChargesViewModel): LiveData<ChargesDTO> = vm.data
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
    override fun extractLiveData(vm: ChargesViewModel): LiveData<ChargesDTO> = vm.data
    override fun extractEntityFromDTO(dto: ChargesDTO): List<Summary> = dto.summary
}

class ChargesFragment : EntityMonthlyFragment<ChargesDTO, ChargesViewModel>() {
    companion object {
        const val INCOME_TAB_INDEX = 0
        const val EXPENSE_TAB_INDEX = 1
        const val SUMMARY_TAB_INDEX = 2
    }

    override val viewModelClass: Class<ChargesViewModel> = ChargesViewModel::class.java
    override val layoutId: Int = R.layout.content_charges
    override val titleId: Int = R.string.charges_name
    override val menuId: Int = R.menu.menu_charges

    override fun getTabFragment(position: Int): Fragment = when (position) {
        INCOME_TAB_INDEX -> ChargeIncomeTab()
        EXPENSE_TAB_INDEX -> ChargeExpenseTab()
        SUMMARY_TAB_INDEX -> ChargeSummaryTab()
        else -> TODO()
    }

    override fun getItemCount(): Int = 3

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        setupFab(view!!.findViewById(R.id.tabs))
        return view
    }

    override fun onDestroyView() {
        hideFab()
        super.onDestroyView()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) =
            when {
                requestCode == REQUEST_CREATE && resultCode == RESULT_OK ->
                    ViewModelProviders.of(activity!!).get(viewModelClass)
                            .addCharge(data!!.getParcelableExtra(RESULT_CREATE))

                else ->
                    super.onActivityResult(requestCode, resultCode, data)
            }

    private fun setupFab(tabLayout: TabLayout) {
        val fab: FloatingActionButton = activity!!.findViewById(R.id.fab)
        val updateVisibility = { position: Int ->
            fab.visibility = if (position == INCOME_TAB_INDEX) VISIBLE else GONE
        }

        updateVisibility(tabLayout.selectedTabPosition)
        fab.setOnClickListener {
            startActivityForResult(Intent(activity, NewChargeActivity::class.java), REQUEST_CREATE)
        }

        tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) { }
            override fun onTabUnselected(tab: TabLayout.Tab?) { }
            override fun onTabSelected(tab: TabLayout.Tab?) = updateVisibility(tab!!.position)
        })
    }

    private fun hideFab() {
        activity!!.findViewById<FloatingActionButton>(R.id.fab).visibility = GONE
    }
}
