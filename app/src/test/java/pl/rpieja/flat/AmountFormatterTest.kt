package pl.rpieja.flat

import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Test
import pl.rpieja.flat.fragment.AmountFormatter
import java.util.*

class AmountFormatterTest {
    var plnFormatter: AmountFormatter = AmountFormatter(Currency.getInstance("PLN"))

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
        assertEquals("-1.25 PLN", plnFormatter.format(-1.25).string)
        assertEquals("10.87 PLN", plnFormatter.format(10.87).string)
    }
}