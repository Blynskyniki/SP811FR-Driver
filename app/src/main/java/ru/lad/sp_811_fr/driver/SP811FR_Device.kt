package ru.lad.sp_811_fr.driver

import android.util.Log
import ru.lad.sp_811_fr.driver.utils.*
import ru.lad.sp_811_fr.driver.utils.constants.Commands
import ru.lad.sp_811_fr.driver.utils.constants.Constants
import java.nio.ByteBuffer
import java.nio.charset.Charset
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class SP811FR_Device(private val transport: Transport) {
    private val charsetEncoder: Charset = Charset.forName("Cp866")

    //  Открыть доумент (продажа\возврат)
    fun openDocument(
        type: DocumentTypes,
        departmentNumber: Number = 1,
        operatorName: String = "Галя",
        documentNumber: Number = 1
    ) {
        var params = byteArrayOf()
//      doc type
        params += type.data
        params += Constants.FS
//      Номер отдела
        params += departmentNumber.toString().toByteArray()
        params += Constants.FS
//      Имя оператора
        params += charsetEncoder.encode(operatorName).toByteArray()
        params += Constants.FS
//      Номер документа
        params += documentNumber.toString().toByteArray()


        val res = requestWrapper(
            CommandGenerator().addCommand(
                Commands.OPEN_DOCUMENT,
                params
            )
        )

        Log.d("SP811", "OPEN_DOCUMENT : " + res.errorMsg)


    }

    // Закрыть доумент (распечатать чек)
    fun closeDocument() {
        var params = byteArrayOf()

//        params = params.plus(" ".toByteArray())
        val res = requestWrapper(
            CommandGenerator().addCommand(
                Commands.CLOSE_DOCUMENT,
                params
            )
        )

        Log.d("SP811", "CLOSE_DOCUMENT : " + res.errorMsg)


    }

    // Печать заголовка для нового документа
    fun printHeader() {
        var params = byteArrayOf()
        val res = requestWrapper(
            CommandGenerator().addCommand(
                Commands.PRINT_HEADER,
                params
            )
        )
        Log.d("SP811", "PRINT_HEADER : " + res.errorMsg)

    }

    // Аннулировать документ
    fun abortDocument() {

        val res = requestWrapper(
            CommandGenerator().addCommand(
                Commands.ABORT_DOCUMENT,
                byteArrayOf()
            )
        )

        Log.d("SP811", "ABORT_DOCUMENT : " + res.errorMsg)


    }

    // Подитог
    fun subTotal() {

        val res = requestWrapper(
            CommandGenerator().addCommand(
                Commands.SUB_TOTAL,
                byteArrayOf()
            )
        )

        Log.d("SP811", "SUB_TOTAL : " + res.errorMsg)


    }

    // Скидка на товар/чек
    fun saleForDocOrProduct(type: Discount, percentOrSum: Int, name: String = "") {
        var params = byteArrayOf()
//      doc type
        params += type.data
        params += Constants.FS
        params += charsetEncoder.encode(name).toByteArray()
        params += Constants.FS
        params += percentOrSum.toString().toByteArray()
        val res = requestWrapper(
            CommandGenerator().addCommand(
                Commands.DISCOUNT,
                params
            )
        )

        Log.e("SP811", "DISCOUNT : " + res.errorMsg)


    }

    // Распечатать отчет без гашения (X-отчет)
    fun xReport() {
        var params = byteArrayOf()
        params = params.plus("01".toByteArray())
        val res = requestWrapper(
            CommandGenerator().addCommand(
                Commands.X_REPORT,
                params
            )
        )

        Log.d("SP811", "X_REPORT : " + res.errorMsg)


    }

    // Распечатать отчет с гашением (Z-отчет)
    fun zReport() {
        var params = byteArrayOf()
        params = params.plus("01".toByteArray())
        val res = requestWrapper(
            CommandGenerator().addCommand(
                Commands.Z_REPORT,
                params
            )
        )

        Log.d("SP811", "Z_REPORT : " + res.errorMsg)


    }

    // Инициализация ФР (Перед любым чеком)
    fun initFR() {
        val date = LocalDateTime.now()

        val formattedDate = date.format(DateTimeFormatter.ofPattern("DDMMYY"))
        val formattedHour = date.format(DateTimeFormatter.ofPattern("HHmmss"))

        var params = formattedDate.toByteArray(Charset.defaultCharset());
        params = params.plus(Constants.FS)
        params = params.plus(formattedHour.toByteArray(Charset.defaultCharset()))
        val res = requestWrapper(
            CommandGenerator().addCommand(
                Commands.INIT,
                params
            )
        )

        Log.d("SP811", "initFR : " + res.errorMsg)


    }

    // Открыть денежный ящик
    fun openCashDrawer() {
        val res = requestWrapper(
            CommandGenerator().addCommand(
                Commands.OPEN_CASH_DRAWER,
                byteArrayOf()
            )
        )

        Log.d("SP811", "OPEN_CASH_DRAWER : " + res.errorMsg)


    }

    // Установить ставки Ндс
    fun setNDS() {

        var params = byteArrayOf()
        params = params.plus("0".toByteArray())
        params = params.plus(Constants.FS)
        params = params.plus("20".toByteArray())
        params = params.plus("1".toByteArray())
        params = params.plus(Constants.FS)
        params = params.plus("10".toByteArray())

        val res = requestWrapper(
            CommandGenerator().addCommand(
                Commands.SET_NDS,
                params
            )
        )

        Log.d("SP811", "initFR : " + res.errorMsg)


    }

    // печать произвольного текста
    fun printText(data: String) {
        val res = requestWrapper(
            CommandGenerator().addCommand(
                Commands.PRINT_TXT,
                charsetEncoder.encode(data)
            )
        )

        Log.d("SP811", res.toString())

    }

    // отложить чек
    // данные удаляются из оперативной памяти ФР и печатается причина отказа от чека.
    fun holdCheck() {
        val res = requestWrapper(
            CommandGenerator().addCommand(
                Commands.HOLD_DOCUMENT,
                charsetEncoder.encode("").toByteArray()
            )
        )

        Log.d("SP811", "HOLD_DOCUMENT $res")

    }

    // печать Копия чека
    fun printCopyCheck() {
        val res = requestWrapper(
            CommandGenerator().addCommand(
                Commands.PRINT_COPY_CHECK,
                byteArrayOf()
            )
        )

        Log.d("SP811", "PRINT_COPY_CHECK $res")

    }

    // Добавить товарную позицию
    fun addProduct(product: Product) {

        val q = ByteBuffer.allocate(2)
        q.toByteArray()
            var params = byteArrayOf()

//          Название товара
            params += charsetEncoder.encode(product.name).toByteArray()
            params += Constants.FS
//           Артикул
            params += charsetEncoder.encode(product.articul).toByteArray()
            params += Constants.FS
//            Количество
            params += product.count.toString().toByteArray()
            params += Constants.FS
//           цена
            params += product.price.toString().toByteArray()
            params += Constants.FS
//          товарной группы
            params += "0".toByteArray()
            params += Constants.FS
            // налог идентификатор
            params += product.nds.toString().toByteArray()
            params += Constants.FS
//            Код товара GTIN
            params += product.gtin.toByteArray()
            params += Constants.FS
            //Тип кода GTIN
            //В этом поле передается тип кода
            //GTIN. В случае пустой строки, ФР
            //посылает в СКНО признак EAN (при
            //наличии Кода товара GTIN)
            params += product.gtinType.toByteArray()


            val res = requestWrapper(

                CommandGenerator().addCommand(
                    Commands.ADD_PRODUCT,
                    params
                )
            )

            Log.d("SP811", "ADD_PRODUCT  : ${res.errorMsg}")

        }




    // Запрос статуса состояния ФР
    fun checkFr() {

        val res = requestWrapper(
            CommandGenerator().addCommand(
                Commands.CHECK_FR,
                byteArrayOf()
            )
        )
        val fatal = String(byteArrayOf(res.data[6])).toInt()
        val current = String(byteArrayOf(res.data[8])).toInt()
        val doc = String(byteArrayOf(res.data[10])).toInt()
        SP_811_FR_CURRENT_ERRORS
        Log.d(
            "SP811",
            "fatal ==> ${getFrFatalErrors(fatal, SP_811_FR_FATAL_ERRORS)}"
        )
        Log.d(
            "SP811",
            "current ==> ${getFrFatalErrors(current, SP_811_FR_CURRENT_ERRORS)}"
        )
        val first = (doc and 0xF0) shr 4
        val second = doc and 0x0F

        Log.d(
            "SP811",
            "doc ==> ${SP_811_FR_DOC_ERRORS[second]}"

        )


        Log.d("SP811", "CHECK_FR : " + res.toString() + "\n" + Utils.getHexString(res.data))


    }

    fun getFrParams(row: Int, column: Int): String {
        var params = byteArrayOf()
        params += row.toString().toByteArray()
        params += Constants.FS
        params += column.toString().toByteArray()
        val res = requestWrapper(
            CommandGenerator().addCommand(
                Commands.GET_FR_PARAMS,
                params
            )
        )
        Log.d("SP811", "GET_FR_PARAMS ${res.errorMsg}")
        Log.d("SP811", "GET_FR_PARAMS ${Utils.getHexString(res.data)}")

        val doc = String(byteArrayOf(res.data[6])).toInt()


        return "$doc";

    }
    fun setFrParams(row: Int, column:Int,value: String) {
        var params = byteArrayOf()
        params += row.toString().toByteArray()
        params += Constants.FS
        params += column.toString().toByteArray()
        params += Constants.FS
        params += value.toByteArray()
        val res = requestWrapper(
            CommandGenerator().addCommand(
                Commands.SET_FR_PARAMS,
                params
            )
        )
        Log.d("SP811", "SET_FR_PARAMS  ${res.errorMsg}")



    }
    fun cashInOutOperation(cashName:String, summOrCount:Int) {
        var params = byteArrayOf()

        params +=  charsetEncoder.encode(cashName).toByteArray()
        params += Constants.FS
        params += summOrCount.toString().toByteArray()
        val res = requestWrapper(
            CommandGenerator().addCommand(
                Commands.CASH_OPERATION,
                params
            )
        )
        Log.d("SP811", "CASH_OPERATION  ${res.errorMsg}")



    }
    fun payment(type:Int,summOrCount:Int,text:String) {
        var params = byteArrayOf()
        params += type.toString().toByteArray()
        params += Constants.FS
        params += summOrCount.toString().toByteArray()
        params += Constants.FS
        params +=  charsetEncoder.encode(text).toByteArray()
        val res = requestWrapper(
            CommandGenerator().addCommand(
                Commands.PAYMENT_OPERATION,
                params
            )
        )
        Log.d("SP811", "PAYMENT_OPERATION  ${res.errorMsg}")



    }



    private fun requestWrapper(command: CommandGenerator): Response {

        this.transport.connect()


        val res = this.transport.sendCommandAndReceiveResponse(command)
        this.transport.closeConnection()
        return res
    }

}

private fun ByteBuffer.toByteArray(): ByteArray {
    val byteArray = ByteArray(this.capacity())
    this.get(byteArray)
    return byteArray
}
