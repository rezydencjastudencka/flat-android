package pl.rpieja.flat.fragment

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.*
import pl.rpieja.flat.R
import pl.rpieja.flat.viewmodels.MonthlyEntityViewModel
import com.twinkle94.monthyearpicker.picker.YearMonthPickerDialog


abstract class EntityMonthlyFragment<T, VM : MonthlyEntityViewModel<T>> : Fragment() {
    companion object {
        private const val YEAR_BUNDLE_KEY = "pl.rpieja.flat.fragment.EntityMonthlyFragment.year"
        private const val MONTH_BUNDLE_KEY = "pl.rpieja.flat.fragment.EntityMonthlyFragment.month"
    }

    abstract val viewModelClass: Class<VM>
    abstract val layoutId: Int
    abstract val titleId: Int
    abstract val menuId: Int

    private var viewModel: VM? = null

    abstract fun getTabFragment(position: Int): Fragment
    abstract fun getItemCount(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    fun showDatePickerDialog() {
        val yearMonthPickerDialog = YearMonthPickerDialog(context, YearMonthPickerDialog.OnDateSetListener { year, month ->
            viewModel!!.load(context!!, month+1, year)
        }, R.style.monthPickerStyle, R.color.colorBackgroundLight)
        yearMonthPickerDialog.show()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(layoutId, container, false)

        (activity!! as AppCompatActivity).supportActionBar!!.title = getString(titleId)

        viewModel = ViewModelProviders.of(activity!!).get(viewModelClass)

        val month = savedInstanceState?.getInt(MONTH_BUNDLE_KEY)
        val year = savedInstanceState?.getInt(YEAR_BUNDLE_KEY)
        if (month == null || year == null) {
            viewModel!!.load(context!!)
        } else {
            viewModel!!.load(context!!, month, year)
        }

        val mSectionsPagerAdapter = object : FragmentPagerAdapter(childFragmentManager) {
            override fun getItem(position: Int): Fragment = getTabFragment(position)
            override fun getCount(): Int = getItemCount()
        }

        val viewPager: ViewPager = view.findViewById(R.id.container)
        val tabLayout: TabLayout = view.findViewById(R.id.tabs)

        viewPager.adapter = mSectionsPagerAdapter
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(viewPager))

        return view
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(MONTH_BUNDLE_KEY, viewModel!!.month)
        outState.putInt(YEAR_BUNDLE_KEY, viewModel!!.year)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater!!.inflate(menuId, menu!!)
    }
}
