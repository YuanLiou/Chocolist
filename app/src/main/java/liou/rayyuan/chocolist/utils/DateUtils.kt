package liou.rayyuan.chocolist.utils

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    const val datePattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"
    val locale = Locale.US

    fun getFormattedDate(date: Date): String {
        return SimpleDateFormat("yyyy/MM/dd", locale).format(date)
    }
}