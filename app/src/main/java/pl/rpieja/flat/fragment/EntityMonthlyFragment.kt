package pl.rpieja.flat.fragment

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import pl.rpieja.flat.R
import pl.rpieja.flat.dialog.MonthPickerDialogFragment
import pl.rpieja.flat.viewmodels.MonthlyEntityViewModel
import pl.rpieja.flat.viewmodels.YearMonth
import java.util.*
import java.util.Calendar.MONTH
import java.util.Calendar.YEAR


abstract class EntityMonthlyFragment<T, VM : MonthlyEntityViewModel<T>> : Fragment() {
    companion object {
        private const val DATE_BUNDLE_KEY = "pl.rpieja.flat.fragment.EntityMonthlyFragment.date"
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

    private fun getTitle(date: YearMonth): String {
        val calendar = Calendar.getInstance()
        calendar.set(YEAR, date.year)
        calendar.set(MONTH, date.month - 1)
        return getString(titleId, calendar)
    }

    fun showDatePickerDialog() {
        MonthPickerDialogFragment().show(fragmentManager, MonthPickerDialogFragment.TAG)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(layoutId, container, false)

        viewModel = ViewModelProviders.of(activity!!).get(viewModelClass)

        viewModel!!.date.observe(this, Observer {
            (activity as AppCompatActivity).supportActionBar!!.title = getTitle(it!!)
        })

        val date: YearMonth? = savedInstanceState?.getParcelable(DATE_BUNDLE_KEY)
        if (date == null) {
            viewModel!!.load(context!!)
        } else {
            viewModel!!.load(context!!, date)
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
        outState.putParcelable(DATE_BUNDLE_KEY, viewModel!!.date.value!!)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater!!.inflate(menuId, menu!!)
    }
}
