package com.kennah.wecatch.local.utils

import java.text.DecimalFormat

object MathUtils {

    fun toDecimal(value: Double?): String {

        val df = DecimalFormat("###.####")
        val formatted = df.format(value)

        val digitBehindDot = formatted.substring(formatted.indexOf(".") + 1)
        return if (digitBehindDot.length == 4) {
            formatted
        } else {
            formatted + "0"
        }
    }

    fun toDecimal5(value: Double?): String {

        val df = DecimalFormat("###.#####")
        val formatted = df.format(value)

        val digitBehindDot = formatted.substring(formatted.indexOf(".") + 1)
        return if (digitBehindDot.length == 5) {
            formatted
        } else {
            formatted + "0"
        }
    }

    fun toDecimal6(value: Double?): String {

        val df = DecimalFormat("###.######")
        val formatted = df.format(value)

        val digitBehindDot = formatted.substring(formatted.indexOf(".") + 1)
        return if (digitBehindDot.length == 6) {
            formatted
        } else {
            formatted + "0"
        }
    }
}