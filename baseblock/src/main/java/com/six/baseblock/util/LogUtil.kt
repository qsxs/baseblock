package com.six.baseblock.util

import android.text.TextUtils
import android.util.Log
import org.json.JSONException
import org.json.JSONObject
import java.io.StringReader
import java.io.StringWriter
import java.util.*
import javax.xml.transform.OutputKeys
import javax.xml.transform.TransformerException
import javax.xml.transform.TransformerFactory
import javax.xml.transform.stream.StreamResult
import javax.xml.transform.stream.StreamSource

/**
 * log工具类
 * Created by lihuabin on 2017/9/14.
 */

object LogUtil {
    private const val CHUNK_SIZE = 4000
    private const val JSON_INDENT = 4
    private const val MIN_STACK_OFFSET = 3

    private var willPrint = true//是否打印
    private var isShowThreadInfo = true//是否显示进程信息
    private val LOCAL_METHOD_COUNT = ThreadLocal<Int>()
    private const val DEFAULT_METHOD_COUNT = 3//默认打印的方法数

    private val mapBoth = HashMap<String, String>()//sql语句需要前后都换行的字段
    private val mapBefore = HashMap<String, String>()//sql语句需要在之前换行的字段
    private val mapAfter = HashMap<String, String>()//sql语句需要在之后换行的字段
    private val mapUpcase = HashMap<String, String>()//sql语句只变大写不换行的字段

    private val methodCount: Int
        get() {
            val count = LOCAL_METHOD_COUNT.get()
            var result = DEFAULT_METHOD_COUNT
            if (count != null) {
                LOCAL_METHOD_COUNT.remove()
                result = count
            }
            if (result < 0) {
                throw IllegalStateException("methodCount cannot be negative")
            }
            return result
        }


    private const val tag = "LogUtil"


    private const val TOP_LEFT_CORNER = '╔'
    private const val BOTTOM_LEFT_CORNER = '╚'
    private const val MIDDLE_CORNER = '╟'
    private const val HORIZONTAL_DOUBLE_LINE = '║'
    private const val DOUBLE_DIVIDER = "════════════════════════════════════════════"
    private const val SINGLE_DIVIDER = "────────────────────────────────────────────"
    private val TOP_BORDER = TOP_LEFT_CORNER + DOUBLE_DIVIDER + DOUBLE_DIVIDER
    private val BOTTOM_BORDER = BOTTOM_LEFT_CORNER + DOUBLE_DIVIDER + DOUBLE_DIVIDER
    private val MIDDLE_BORDER = MIDDLE_CORNER + SINGLE_DIVIDER + SINGLE_DIVIDER

    init {
        mapBoth["select"] = "select"
        mapBoth["delete"] = "delete"
        mapBoth["set"] = "set"
        mapBoth["update"] = "update"
        mapBoth["having"] = "having"
        mapBoth["from"] = "from"
        mapBoth["where"] = "where"
        mapBoth["and"] = "and"
        mapBoth["or"] = "or"
        mapBoth["not"] = "not"
        mapBoth["like"] = "like"
        mapBoth["in"] = "in"

        mapBefore["insert"] = "insert"
        mapBefore["drop"] = "drop"
        mapBefore["alter"] = "alter"
        mapBefore["left"] = "left"
        mapBefore["right"] = "right"
        mapBefore["group"] = "group"
        mapBefore["order"] = "order"
        mapBefore["natural"] = "natural"
        mapBefore["create"] = "create"
        mapBefore[")"] = ")"

        mapAfter["join"] = "join"
        mapAfter["into"] = "into"
        mapAfter["table"] = "table"
        mapAfter["by"] = "by"
        mapAfter["("] = "("

        mapUpcase["avg"] = "avg"
        mapUpcase["min"] = "min"
        mapUpcase["max"] = "max"
        mapUpcase["sum"] = "sum"
        mapUpcase["count"] = "count"
        mapUpcase["avg("] = "avg("
        mapUpcase["min("] = "min("
        mapUpcase["max("] = "max("
        mapUpcase["sum("] = "sum("
        mapUpcase["count("] = "count("
        mapUpcase["distinct"] = "distinct"
        mapUpcase["all"] = "all"
        mapUpcase["as"] = "as"
        mapUpcase["desc"] = "desc"
        mapUpcase["asc"] = "asc"
        mapUpcase["unique"] = "unique"
    }

    /**
     * 初始化
     *
     * @param willPrint      是否打印
     * @param showThreadInfo 打印时时候打印进程信息
     */
    fun init(willPrint: Boolean, showThreadInfo: Boolean) {
        LogUtil.willPrint = willPrint
        isShowThreadInfo = showThreadInfo
    }

    fun si(message: String) {
        si(tag, message)
    }

    fun si(tag: String, message: String) {
        Log.i(tag, message)
    }

    fun i(message: String, vararg args: Any) {
        log(Log.INFO, message, *args)
    }

    fun d(message: String, vararg args: Any) {
        log(Log.DEBUG, message, *args)
    }

    fun e(message: String, vararg args: Any) {
        log(Log.ERROR, message, *args)
    }

    fun e(msg: String?, throwable: Throwable?, vararg args: Any) {
        var message = msg
        if (throwable != null && message != null) {
            message += " : " + throwable.toString()
        }
        if (throwable != null && message == null) {
            message = throwable.toString()
        }
        if (message == null) {
            message = "No message/exception is set"
        }
        log(Log.ERROR, message, *args)
    }

    fun w(message: String, vararg args: Any) {
        log(Log.WARN, message, *args)
    }

    fun wtf(message: String, vararg args: Any) {
        log(Log.ASSERT, message, *args)
    }

    fun simlpeJson(tag: String, json: String) {
        if (TextUtils.isEmpty(json)) {
            si(tag, json)
            return
        }
        try {
            val jsonObject = JSONObject(json)
            val message = jsonObject.toString(JSON_INDENT)
            si(tag, message)
        } catch (e: JSONException) {
            si(tag, json)
        }
    }

    fun json(json: String) {
        if (TextUtils.isEmpty(json)) {
            d("Empty/Null json content")
            return
        }
        try {
            val jsonObject = JSONObject(json)
            val message = jsonObject.toString(JSON_INDENT)
            d(message)
        } catch (e: JSONException) {
            d(json)
        }

    }

    @Throws(JSONException::class)
    fun jsonThrowExceptoin(json: String) {
        if (TextUtils.isEmpty(json)) {
            d("Empty/Null json content")
            return
        }

        val jsonObject = JSONObject(json)
        val message = jsonObject.toString(JSON_INDENT)
        d(message)
    }

    fun xml(xml: String) {
        if (TextUtils.isEmpty(xml)) {
            d("Empty/Null xml content")
            return
        }
        try {
            val xmlInput = StreamSource(StringReader(xml))
            val xmlOutput = StreamResult(StringWriter())
            val transformer = TransformerFactory.newInstance().newTransformer()
            transformer.setOutputProperty(OutputKeys.INDENT, "yes")
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2")
            transformer.transform(xmlInput, xmlOutput)
            d(xmlOutput.writer.toString().replaceFirst(">".toRegex(), ">\n"))
        } catch (e: TransformerException) {
            e(e.cause?.message + "\n" + xml)
        }

    }

    fun sql(sql: String) {
        if (TextUtils.isEmpty(sql)) {
            d("Empty/Null sql content")
            return
        }
        val sqlArray = sql.trim { it <= ' ' }.split("\\s+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        var hasBetween = false
        for (i in sqlArray.indices) {
            val s = sqlArray[i]
            val sLowerCase = s.toLowerCase()
            if (sLowerCase == "between") {
                hasBetween = true
                sqlArray[i] = s.toUpperCase()
            } else if (hasBetween && sLowerCase == "and") {
                //between后的第一个and不换行
                sqlArray[i] = s.toUpperCase()
                hasBetween = false
            } else if (mapBoth.containsKey(sLowerCase)) {
                sqlArray[i] = "\n" + s.toUpperCase() + "\n\t"
            } else if (mapBefore.containsKey(sLowerCase)) {
                sqlArray[i] = "\n" + s.toUpperCase()
            } else if (mapAfter.containsKey(sLowerCase)) {
                sqlArray[i] = s.toUpperCase() + "\n\t"
            } else if (mapUpcase.containsKey(sLowerCase)) {
                sqlArray[i] = s.toUpperCase()
            }
        }

        val formatSQL = Arrays.toString(sqlArray)
                .replace("\\[".toRegex(), "")//Arrays.toString后会用一个[]包着，这里把他去掉
                .replace("]".toRegex(), "")//Arrays.toString后会用一个[]包着，这里把他去掉
                .replace(",,".toRegex(), "#@#@")//Arrays.toString后每个元素后面会加一个逗号，原先的一个逗号会变成两个，先把原先逗号换成#@#@
                .replace(",".toRegex(), "")//把多余的逗号去掉
                .replace("#@#@".toRegex(), ",")//吧原先的逗号还原
        d(formatSQL)
    }


    /**
     * This method is synchronized in order to avoid messy of logs' order.
     *
     * @param args 用于格式化msg,详情[.createMessage]
     */
    @Synchronized
    private fun log(logType: Int, msg: String, vararg args: Any) {
        if (!willPrint) {
            return
        }
        val tag = tag
        val methodCount = methodCount
        val message = createMessage(msg, *args)
        logTopBorder(logType, tag)
        logHeaderContent(logType, tag, methodCount)

        //get bytes of message with system's default charset (which is UTF-8 for Android)
        val bytes = message.toByteArray()
        val length = bytes.size
        if (length <= CHUNK_SIZE) {
            if (methodCount > 0) {
                logDivider(logType, tag)
            }
            logContent(logType, tag, message)
            logBottomBorder(logType, tag)
            return
        }
        if (methodCount > 0) {
            logDivider(logType, tag)
        }
        var i = 0
        while (i < length) {
            val count = Math.min(length - i, CHUNK_SIZE)
            //create a new String with system's default charset (which is UTF-8 for Android)
            logContent(logType, tag, String(bytes, i, count))
            i += CHUNK_SIZE
        }
        logBottomBorder(logType, tag)
    }


    /**
     * 打印顶部边框
     */
    private fun logTopBorder(logType: Int, tag: String) {
        logChunk(logType, tag, TOP_BORDER)
    }

    /**
     * 打印头部内容（线程和方法）
     */
    private fun logHeaderContent(logType: Int, tag: String, methodCount: Int) {
        @Suppress("NAME_SHADOWING")
        var methodCount = methodCount
        val trace = Thread.currentThread().stackTrace
        if (isShowThreadInfo) {
            logChunk(logType, tag,
                    HORIZONTAL_DOUBLE_LINE + " Thread: " + Thread.currentThread().name
                            + "   id:" + Thread.currentThread().id
                            + "   Priority:" + Thread.currentThread().priority)
            logDivider(logType, tag)
        }
        val level = ""

        val stackOffset = getStackOffset(trace)

        //corresponding method count with the current stack may exceeds the stack trace. Trims the count
        if (methodCount + stackOffset > trace.size) {
            methodCount = trace.size - stackOffset - 1
        }

        for (i in 1..methodCount) {
            val stackIndex = i + stackOffset
            if (stackIndex >= trace.size) {
                continue
            }
            val builder = StringBuilder()
            builder.append("║ ")
                    .append(level)
                    .append(getSimpleClassName(trace[stackIndex].className))
                    .append(".")
                    .append(trace[stackIndex].methodName)
                    .append(" ")
                    .append(" (")
                    .append(trace[stackIndex].fileName)
                    .append(":")
                    .append(trace[stackIndex].lineNumber)
                    .append(")")
            //            level += "   ";
            logChunk(logType, tag, builder.toString())
        }
    }

    /**
     * 打印底部边框
     */
    private fun logBottomBorder(logType: Int, tag: String) {
        logChunk(logType, tag, BOTTOM_BORDER)
    }

    /**
     * 打印分割线
     */
    private fun logDivider(logType: Int, tag: String) {
        logChunk(logType, tag, MIDDLE_BORDER)
    }

    /**
     * 打印内容
     */
    private fun logContent(logType: Int, tag: String, chunk: String) {
        val lines = chunk.split(System.getProperty("line.separator").toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        for (line in lines) {
            logChunk(logType, tag, "$HORIZONTAL_DOUBLE_LINE $line")
        }
    }

    /**
     * 根据logType调用相应的android.util.Log的打印方法
     *
     * @param logType 打印界别
     */
    private fun logChunk(logType: Int, tag: String, chunk: String) {
        val finalTag = formatTag(tag)
        when (logType) {
            Log.ERROR -> Log.e(finalTag, chunk)
            Log.INFO -> Log.i(finalTag, chunk)
            Log.VERBOSE -> Log.v(finalTag, chunk)
            Log.WARN -> Log.w(finalTag, chunk)
            Log.ASSERT -> Log.wtf(finalTag, chunk)
            Log.DEBUG -> Log.d(finalTag, chunk)
        // Fall through, log debug by default
            else -> Log.d(finalTag, chunk)
        }
    }

    /**
     * tag
     */
    private fun formatTag(tag: String): String {
        return if (!TextUtils.isEmpty(tag) && !TextUtils.equals(LogUtil.tag, tag)) {
            LogUtil.tag + "-" + tag
        } else LogUtil.tag
    }

    /**
     * Determines the starting index of the stack trace, after method calls made by this class.
     *
     * @param trace the stack trace
     * @return the stack offset
     */
    private fun getStackOffset(trace: Array<StackTraceElement>): Int {
        var i = MIN_STACK_OFFSET
        while (i < trace.size) {
            val e = trace[i]
            val name = e.className
            if (name != LogUtil::class.java.name) {
                return --i
            }
            i++
        }
        return -1
    }

    private fun getSimpleClassName(name: String): String {
        val lastIndex = name.lastIndexOf(".")
        return name.substring(lastIndex + 1)
    }

    /**
     * 格式化message 详情 http://blog.csdn.net/lonely_fireworks/article/details/7962171
     */
    private fun createMessage(message: String, vararg args: Any): String {
        return if (args.isEmpty()) message else String.format(message, *args)
    }

}
