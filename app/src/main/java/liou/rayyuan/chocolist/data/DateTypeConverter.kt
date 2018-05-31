package liou.rayyuan.chocolist.data

import android.arch.persistence.room.TypeConverter
import liou.rayyuan.chocolist.utils.DateUtils
import java.text.SimpleDateFormat
import java.util.*

class DateTypeConverter {

    @TypeConverter
    fun convertStringToDate(value: String): Date {
        val dateFormat = SimpleDateFormat(DateUtils.datePattern, DateUtils.locale)
        return dateFormat.parse(value)
    }

    @TypeConverter
    fun convertDateToString(date: Date): String {
        val dateFormat = SimpleDateFormat(DateUtils.datePattern, DateUtils.locale)
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
        return dateFormat.format(date)
    }

}