package pl.rpieja.flat

import org.junit.Assert.assertEquals
import org.junit.Test
import pl.rpieja.flat.fragment.AmountFormatter
import java.util.*

class AmountFormatterTest {
    private val plnFormatter: AmountFormatter = AmountFormatter(
            Locale.forLanguageTag("pl-PL"),
            Currency.getInstance("PLN")
    )

    @Test
    fun greenForNegative() {
        assertEquals(R.color.amountNegative, plnFormatter.format(-20.0).color)
    }

    @Test
    fun redForPositive() {
        assertEquals(R.color.amountPositive, plnFormatter.format(131.2).color)
    }

    @Test
    fun neutralForZero() {
        assertEquals(R.color.amountNeutral, plnFormatter.format(0.0).color)
    }

    @Test
    fun neutralNearZero() {
        assertEquals(R.color.amountNeutral, plnFormatter.format(0.005).color)
        assertEquals(R.color.amountNeutral, plnFormatter.format(-0.005).color)
    }

    @Test
    fun preservesSign() {
        assertEquals("-1,25 zł", plnFormatter.format(-1.25).value)
        assertEquals("10,87 zł", plnFormatter.format(10.87).value)
    }

    @Test
    fun englishLocale() {
        val enUsdFormatter = AmountFormatter(Locale.forLanguageTag("en-US"))
        assertEquals("$21.37", enUsdFormatter.format(21.37).value)
        assertEquals("($133.37)", enUsdFormatter.format(-133.37).value)
    }
}