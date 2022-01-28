package ru.lad.sp_811_fr.driver.utils

import android.util.Log

class Response(val data: ByteArray) {
    var errorMsg: String? = ""
    val isError
        get() = errorMsg?.length!! > 0

    var frStatus1:String? =null
    var frStatus2:String? =null
    var frStatus3:String? =null


    //    console.log('----------------->');
//    console.log('new data', data.toString('hex'), '===>', data.toString('utf8'));
//    console.log('STX', data[0].toString(10));
//    console.log('ID', data[1].toString(16));
//    console.log('COMMAND', data[2].toString(16), data[3].toString(16));
//    console.log('ERROR_CODE', data[4].toString(16), data[5].toString(16), '===>', checkErrorByBytesArray(data.slice(4, 6)));
//    console.log('DATA', data.slice(6, data.length - 2).toString('hex'));
//    console.log('ETX', data[data.length - 3].toString(16));
//    console.log('CRC', data.slice(data.length - 2, data.length).toString('hex'));
    init {
//        Log.d("SP811", "===========================>")
//        Log.d("SP811", "Response ==> ${Utils.getHexString(data)} \nERROR_CODE ${Utils.getHexString(data[4])} ${Utils.getHexString(data[5])}")
//        Log.d("SP811", "STX ==> ${Utils.getHexString(data[0])}")
//        Log.d("SP811", "ID ==> ${Utils.getHexString(data[1])}")
//        Log.d("SP811", "COMMAND ==> ${Utils.getHexString(data[2])} ${Utils.getHexString(data[3])}")
//        Log.d(
//            "SP811",
//            "ERROR_CODE ==> ${Utils.getHexString(data[4])} ${Utils.getHexString(data[5])} ==> ${
//                makeErrorByCodeInBytes(
//                    byteArrayOf(data[4], data[5])
//                )
//            }"
//        )
//        Log.d(
//            "SP811",
//            "DATA ==> ${Utils.getHexString(data.sliceArray(6..data.size-4))}"
//        )
//
        this.errorMsg = makeErrorByCodeInBytes(
            byteArrayOf(data[4], data[5])
        )
//        Log.d("SP811", "===========================>")

    }

    override fun toString(): String {

        return this.errorMsg.orEmpty()
    }
}