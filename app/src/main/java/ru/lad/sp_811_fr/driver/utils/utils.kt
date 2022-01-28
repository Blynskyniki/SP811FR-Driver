package ru.lad.sp_811_fr.driver.utils

import android.util.Log

fun stringToASCII(data: String): ByteArray {
    return data.map { symbol ->
        return@map symbol.code.toByte()
    }.toByteArray()
}

fun toBinaryString(x: Int, len: Int): String {
    return String.format(
        "%" + len + "s",
        Integer.toBinaryString(x)
    ).replace(" ".toRegex(), "0")
}

fun checkBitInInt(n: Int, position: Int): Boolean {

    return n and (1 shl position) != 0

}