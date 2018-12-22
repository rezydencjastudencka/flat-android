package pl.rpieja.flat.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import pl.rpieja.flat.R
import pl.rpieja.flat.dto.Expense
import pl.rpieja.flat.dto.Revenue
import pl.rpieja.flat.dto.Summary
import pl.rpieja.flat.fragment.ChargesFragment
import pl.rpieja.flat.viewmodels.ChargesViewModel

data class SortOption<T>(val description: String, val comparator: Comparator<T>)

class ChargesSortDialog(private val context: Context, private val viewModel: ChargesViewModel) {
    private val chargesSortOptions: List<SortOption<Revenue>> = listOf(
            SortOption(context.getString(R.string.date),
                    Comparator { o1, o2 -> o1.date.compareTo(o2.date) }),
            SortOption(context.getString(R.string.name),
                    Comparator { o1, o2 -> o1.name.compareTo(o2.name) }),
            SortOption(context.getString(R.string.amount),
                    Comparator { o1, o2 -> o1.amount.compareTo(o2.amount) })
    )

    private val incomesSortOptions: List<SortOption<Expense>> = listOf(
            SortOption(context.getString(R.string.date),
                    Comparator { o1, o2 -> o1.date.compareTo(o2.date) }),
            SortOption(context.getString(R.string.name),
                    Comparator { o1, o2 -> o1.name.compareTo(o2.name) }),
            SortOption(context.getString(R.string.user),
                    Comparator { o1, o2 -> o1.from.name.compareTo(o2.from.name) }),
            SortOption(context.getString(R.string.amount),
                    Comparator { o1, o2 -> o1.amount.compareTo(o2.amount) })
    )

    private val summarySortOptions: List<SortOption<Summary>> = listOf(
            SortOption(context.getString(R.string.user),
                    Comparator { o1, o2 -> o1.user.name.compareTo(o2.user.name) }),
            SortOption(context.getString(R.string.amount),
                    Comparator { o1, o2 -> o1.amount.compareTo(o2.amount) })
    )

    private fun <T> dialog(options: List<SortOption<T>>, action: (Comparator<T>) -> Unit): Dialog {
        val items = options.map { x -> x.description }.toTypedArray()
        val listener = DialogInterface.OnClickListener { _, y -> action(options[y].comparator) }

        return AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.sort_order_title))
                .setItems(items, listener)
                .create()
    }

    private fun revenuesSortDialog(): Dialog =
            dialog(chargesSortOptions) { c -> viewModel.sortRevenues(c) }

    private fun expensesSortDialog(): Dialog =
            dialog(incomesSortOptions) { c -> viewModel.sortExpenses(c) }

    private fun summarySortDialog(): Dialog =
            dialog(summarySortOptions) { c -> viewModel.sortSummary(c) }

    fun create(tabNum: Int): Dialog? =
            when (tabNum) {
                ChargesFragment.REVENUE_TAB_INDEX -> revenuesSortDialog()
                ChargesFragment.EXPENSE_TAB_INDEX -> expensesSortDialog()
                ChargesFragment.SUMMARY_TAB_INDEX -> summarySortDialog()
                else -> TODO()
            }
}