package ru.lad.sp_811_fr.driver.utils

import ru.lad.sp_811_fr.driver.utils.Utils.getCRC
import java.nio.Buffer
import java.nio.ByteBuffer
import java.util.zip.CRC32
import kotlin.experimental.and
import kotlin.experimental.xor

fun makeCRC(bytes: ByteArray): Byte {
    var sum = 0
    for (i in 0..bytes.size - 1) {
        sum = sum xor (bytes[i].toInt() and 0xFF)
    }

    return (sum and 0xFF).toByte()
}

