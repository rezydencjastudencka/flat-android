package pl.rpieja.flat.fragment

import android.support.annotation.ColorRes
import pl.rpieja.flat.R
import java.util.*

data class SummaryFormat(@ColorRes val color: Int, val string: String)

class AmountFormatter(currency: Currency) {
    val currencyFormatter = Formatter

    fun format(amount: Double): SummaryFormat {
        val color: Int = when {
            amount >= 0.01 -> R.color.amountPositive
            amount <= -0.01 -> R.color.amountNegative
            else -> R.color.amountNeutral
        }

        return SummaryFormat(color, amount.toString())
    }

}
