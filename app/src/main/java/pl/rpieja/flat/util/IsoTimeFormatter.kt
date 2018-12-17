package pl.rpieja.flat.util

import java.text.SimpleDateFormat
import java.util.*

object IsoTimeFormatter {

    fun toIso8601(date: Date): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
        return dateFormat.format(date)
    }

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
