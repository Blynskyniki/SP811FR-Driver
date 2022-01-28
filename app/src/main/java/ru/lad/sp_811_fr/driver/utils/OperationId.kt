package ru.lad.sp_811_fr.driver.utils

import android.util.Log

fun makeOperationId(): Byte {
    val id =     (21..80).random().toByte()
//    Log.d("SP811", "id: ${id}")

    return id

}