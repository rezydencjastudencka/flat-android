package pl.rpieja.flat.util

import java.text.SimpleDateFormat
import java.util.*

object IsoTimeFormatter {

    fun toGraphqlDate(date: Date): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
        return dateFormat.format(date)
    }

    fun fromGraphqlDate(date: String): Date {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        return dateFormat.parse(date)
    }
}
