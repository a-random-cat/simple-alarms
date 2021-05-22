package cat.simple.alarms

import android.text.TextUtils
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class HelperTime {
    companion object {
        private fun addToList(arrayList:ArrayList<String>, name:String, count:Long) {
            if (count > 0) {
                arrayList.add(count.toString())
                arrayList.add(if (count>1) "${name}s" else name)
            }
        }

        fun getTimeString(milliSeconds:Long):String? {
            val formatter = SimpleDateFormat("dd MMM yyyy, HH:mm:ss")

            val calendar: Calendar = Calendar.getInstance()
            calendar.timeInMillis = milliSeconds
            return formatter.format(calendar.time)
        }

        fun getDurationBreakdown(mils: Long): String? {
            val arrayList = ArrayList<String>()
            var millis = mils
            require(millis >= 0) { "Duration must be greater than zero!" }
            val days: Long = TimeUnit.MILLISECONDS.toDays(millis)
            millis -= TimeUnit.DAYS.toMillis(days)
            val hours: Long = TimeUnit.MILLISECONDS.toHours(millis)
            millis -= TimeUnit.HOURS.toMillis(hours)
            val minutes: Long = TimeUnit.MILLISECONDS.toMinutes(millis)
            millis -= TimeUnit.MINUTES.toMillis(minutes)
            val seconds: Long = TimeUnit.MILLISECONDS.toSeconds(millis)
            addToList(arrayList, "day", days)
            addToList(arrayList, "hr", hours)
            addToList(arrayList, "min", minutes)

            return TextUtils.join(" ", arrayList)
        }
    }
}