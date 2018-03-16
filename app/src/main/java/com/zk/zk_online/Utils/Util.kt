package com.zk.lpw.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.widget.EditText
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by mukun on 2017/8/18.
 */
class Util {
    companion object {
        fun goActivity(context: Context, activity: Class<*>, bundle: Bundle?, isFinish: Boolean) {
            val intent = Intent()
            intent.setClass(context, activity)
            if (bundle != null) {
                intent.putExtras(bundle)
            }
            context.startActivity(intent)
            if (isFinish) {
                (context as Activity).finish()
            }
        }

        fun goActivityForResult(context: Context, activity: Class<*>, bundle: Bundle?, requestCode: Int, isFinish: Boolean) {
            val intent = Intent()
            intent.setClass(context, activity)
            if (bundle != null) {
                intent.putExtras(bundle)
            }
            (context as Activity).startActivityForResult(intent, requestCode)
            if (isFinish) {
                (context).finish()
            }
        }

        fun goResult(context: Context, bundle: Bundle?, resultCode: Int) {
            val intent = (context as Activity).intent
            if (bundle != null) {
                intent.putExtras(bundle)
            }
            (context).setResult(resultCode, intent)
            (context).finish()
        }

        fun getCurrentTime(format: String): String {
            val date = Date()
            val sdf = SimpleDateFormat(format, Locale.getDefault())
            return sdf.format(date)
        }

        /**
         * 保留小数点后两位
         */
        fun getDecimalPoint2(obj: Any): String {
            return String.format("%.2f", obj)
        }

        fun getDecimalPoint3(obj: Any): Float {
            return java.lang.Float.valueOf(String.format("%.2f", obj))!!
        }

        /**
         * 取整
         */
        fun getDecimalPoint(obj: Any): String {
            return String.format("%.0f", obj)
        }

        /**
         * 删除接口返回时间类型里的T
         *
         * @param str
         * @return
         */
        fun getDataClarT(str: String): String {
            return str.replace("T", " ")
        }

        /**
         * 小数点后最多输入两位
         * @param editText
         */
        fun setPricePoint(editText: EditText) {
            editText.addTextChangedListener(object : TextWatcher {

                override fun onTextChanged(s: CharSequence, start: Int, before: Int,
                                           count: Int) {
                    var s = s
                    if (s.toString().contains(".")) {
                        if (s.length - 1 - s.toString().indexOf(".") > 2) {
                            s = s.toString().subSequence(0,
                                    s.toString().indexOf(".") + 3)
                            editText.setText(s)
                            editText.setSelection(s.length)
                        }
                    }
                    if (s.toString().trim { it <= ' ' }.substring(0) == ".") {
                        s = "0" + s
                        editText.setText(s)
                        editText.setSelection(2)
                    }

                    if (s.toString().startsWith("0") && s.toString().trim { it <= ' ' }.length > 1) {
                        if (s.toString().substring(1, 2) != ".") {
                            editText.setText(s.subSequence(0, 1))
                            editText.setSelection(1)
                            return
                        }
                    }
                }

                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

                }

                override fun afterTextChanged(s: Editable) {

                }
            })
        }

        fun isEmptyOrNull(str: String?): Boolean {
            if (str == null) {
                return false
            }
            if (TextUtils.isEmpty(str)) {
                return false
            }
            if (str == "null") {
                return false
            }
            return true
        }

        /**
         * 日期转字符串
         *
         * @param date
         * @param dFormat
         * @return
         */
        @SuppressLint("SimpleDateFormat")
        fun convertDateToString(date: Date, dFormat: String): String {
            val dateFormat = SimpleDateFormat(dFormat)
            return dateFormat.format(date)
        }

        /**
         * 获取当周的第一天
         */
        fun getWeekStratDate(): Date {
            val currentDate = GregorianCalendar()
            currentDate.firstDayOfWeek = Calendar.MONDAY
            val day = currentDate.get(Calendar.DAY_OF_WEEK)
            currentDate.add(Calendar.DATE, currentDate.firstDayOfWeek - day)
            return currentDate.time.clone() as Date
        }

        /*
     * 获取当周的最后一天
     */
        fun getWeekEndDate(): Date {
            val currentDate = GregorianCalendar()
            currentDate.firstDayOfWeek = Calendar.MONDAY
            val day = currentDate.get(Calendar.DAY_OF_WEEK)
            currentDate.add(Calendar.DATE, currentDate.firstDayOfWeek - day)
            currentDate.add(Calendar.DATE, 6)
            return currentDate.time
        }

        /**
         * 获取当月的第一天
         */
        fun getMonthStratDay(): Date {
            val c = Calendar.getInstance()
            c.set(Calendar.DAY_OF_MONTH, 1)
            return c.time
        }

        /**
         * 获取当月的最后一天
         */
        fun getMonthEndDate(): Date {
            val cal = Calendar.getInstance()
            cal.set(Calendar.DAY_OF_MONTH,
                    cal.getActualMaximum(Calendar.DAY_OF_MONTH))
            return cal.time
        }

        fun getYestaday(): String {
            val cal = Calendar.getInstance()
            cal.add(Calendar.DATE, -1)
            var yesterday = SimpleDateFormat("yyyy-MM-dd").format(cal.time)
            return yesterday
        }

        fun getPreviousMonthFirst(): String {
            var str = ""
            val sdf = SimpleDateFormat("yyyy-MM-dd")

            val lastDate = Calendar.getInstance()
            lastDate.set(Calendar.DATE, 1)// 设为当前月的1号
            lastDate.add(Calendar.MONTH, -1)// 减一个月，变为下月的1号
            str = sdf.format(lastDate.time)
            return str
        }

        fun getPreviousMonthEnd(): String {
            var str = ""
            val sdf = SimpleDateFormat("yyyy-MM-dd")

            val lastDate = Calendar.getInstance()
            lastDate.add(Calendar.MONTH, -1)// 减一个月
            lastDate.set(Calendar.DATE, 1)// 把日期设置为当月第一天
            lastDate.roll(Calendar.DATE, -1)// 日期回滚一天，也就是本月最后一天
            str = sdf.format(lastDate.time)
            return str
        }

        /**
         * 日期字符串例如 2015-3-10  Num:需要减少的日例如 7
         */
        fun getDateStrMonth(day: String, Num: Int): String {
            val df = SimpleDateFormat("yyyy-MM-dd")
            var nowDate: Date? = null
            try {
                nowDate = df.parse(day)
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            val rightNow = Calendar.getInstance()
            rightNow.time = nowDate
            rightNow.add(Calendar.MONTH, Num)//日期加3个月
            val dt1 = rightNow.time
            return df.format(dt1)
        }
    }
}