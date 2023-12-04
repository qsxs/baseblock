package com.lihb.baseblock.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

private val sdf = (SimpleDateFormat.getInstance() as SimpleDateFormat)

object DateUtil {

    fun currentDateTimeString(): String {
        sdf.applyPattern("yyyy-MM-dd HH:mm")
        return sdf.format(System.currentTimeMillis())
    }
}

fun String?.formatDate(
    parsePattern: String = "yyyy-MM-dd HH:mm",
    sourcePattern: String = "y-M-d H:m:s.S Z"
): String? {
    if (this.isNullOrEmpty()) return this
    sdf.applyPattern(sourcePattern)
    val parse = sdf.parse(this.replace("T", " ").replace("+", " +")) ?: return this
    sdf.applyPattern(parsePattern)
    return sdf.format(parse)
}

fun Date?.formatDate(
    parsePattern: String = "yyyy-MM-dd HH:mm",
): String? {
    if (this == null) return ""
    sdf.applyPattern(parsePattern)
    return sdf.format(this)
}


/**
 * 把日期格式化为人类易读的格式 比如今天 20:00; 昨天09:44; 前天18:00
 * @param parsePattern 前天以前的时间要被格式化的格式
 * @param sourcePattern 原始的时间格式
 */
fun String?.formatDateFriendly(
    parsePattern: String = "yyyy-MM-dd HH:mm",
    sourcePattern: String = "y-M-d H:m:s.S Z"
): String? {
    if (this.isNullOrEmpty()) return this
    sdf.applyPattern(sourcePattern)
    val parse = sdf.parse(this.replace("T", " ").replace("+", " +")) ?: return this
    return parse.formatDateFriendly(parsePattern)
}

/**
 * 把日期格式化为人类易读的格式 比如今天 20:00; 昨天09:44; 前天18:00
 * @param parsePattern 前天以前的时间要被格式化的格式
 */
fun Date?.formatDateFriendly(
    parsePattern: String = "yyyy-MM-dd HH:mm",
): String? {
    if (this == null) return ""
    val now = Date()
    val calendar = Calendar.getInstance()
    calendar.time = now
    val dayNow = calendar.get(Calendar.DAY_OF_YEAR)
    calendar.time = this
    val daySource = calendar.get(Calendar.DAY_OF_YEAR)
    return when {
        daySource == dayNow -> {//同一天
            sdf.applyPattern("HH:mm")
            sdf.format(this)
        }

        dayNow - daySource == 1 -> {
            //昨天
            sdf.applyPattern("HH:mm")
            "昨天${sdf.format(this)}"
        }

        dayNow - daySource == 2 -> {
            //前天
            sdf.applyPattern("HH:mm")
            "前天${sdf.format(this)}"
        }

        else -> {
            //比前天更远的时间
            sdf.applyPattern(parsePattern)
            sdf.format(this)
        }
    }
}