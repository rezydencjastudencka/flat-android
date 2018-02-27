package pl.rpieja.flat.fragment

import android.support.annotation.ColorRes
import pl.rpieja.flat.R
import java.text.NumberFormat
import java.util.*

data class AmountFormat(@ColorRes val color: Int, val value: String)

class AmountFormatter(locale: Locale = Locale.getDefault(),
                      currency: Currency = Currency.getInstance(locale)) {
    private val numberFormat: NumberFormat = NumberFormat.getCurrencyInstance(locale)

    init {
        numberFormat.currency = currency
    }

    fun format(amount: Double): AmountFormat {
        val color: Int = when {
            amount >= 0.01 -> R.color.amountPositive
            amount <= -0.01 -> R.color.amountNegative
            else -> R.color.amountNeutral
        }

        return AmountFormat(color, numberFormat.format(amount))
    }

}
