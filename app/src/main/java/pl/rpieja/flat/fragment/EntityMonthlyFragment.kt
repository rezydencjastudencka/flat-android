package pl.rpieja.flat.fragment

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.*
import com.rackspira.kristiawan.rackmonthpicker.RackMonthPicker
import pl.rpieja.flat.R
import pl.rpieja.flat.viewmodels.MonthlyEntityViewModel
import java.util.*

abstract class EntityMonthlyFragment<T, VM: MonthlyEntityViewModel<T>>: Fragment() {
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

    fun showDatePickerDialog() = RackMonthPicker(context)
            .setLocale(Locale.ENGLISH)
            .setColorTheme(R.color.colorPrimaryDark)
            .setSelectedMonth(viewModel!!.month - 1)
            .setPositiveButton({ month, _, _, year, _ -> viewModel!!.load(context!!, month, year) })
            .setNegativeButton({ picker -> picker.dismiss() })
            .show()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(layoutId, container, false)

        (activity!! as AppCompatActivity).supportActionBar!!.title = getString(titleId)

        viewModel = ViewModelProviders.of(activity!!).get(viewModelClass)
        viewModel!!.load(context!!)

        val mSectionsPagerAdapter = object: FragmentPagerAdapter(childFragmentManager) {
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


    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater!!.inflate(menuId, menu!!)
    }
}
