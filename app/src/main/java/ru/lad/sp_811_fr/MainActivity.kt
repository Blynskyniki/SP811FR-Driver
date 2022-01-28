package ru.lad.sp_811_fr

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ru.lad.sp_811_fr.driver.utils.Transport
import java.lang.Exception
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.KeyboardType
import ru.lad.sp_811_fr.driver.SP811FR_Device
import ru.lad.sp_811_fr.driver.utils.Discount
import ru.lad.sp_811_fr.driver.utils.DocumentTypes
import ru.lad.sp_811_fr.driver.utils.Product


class MainActivity : ComponentActivity() {
    val Yellow200 = Color(0xffffeb46)
    val Blue200 = Color(0xff91a4fc)
    val DarkColors = darkColors(
        primary = Yellow200,
        secondary = Blue200,
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            settingsMenu()
        }
    }

    @Composable
    fun settingsMenu() {
        var ip by remember { mutableStateOf("192.168.2.150") }
        var port by remember { mutableStateOf("9876") }

        makeMaterialColumn() {
            TopAppBar(
                title = {
                    Text("Настройки подключения ")
                },
                actions = {
                    IconButton(onClick = {
                        Thread {
                            try {

//                                driver.abortDocument()

                            } catch (e: Exception) {
                                e.message?.let { Log.e("SP811", it) }
                            }
                        }.start()
                    }) {
                        Icon(Icons.Filled.Clear, contentDescription = "")
                    }

                },
                contentColor = Color.White,

                )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {


                TextField(
                    value = ip,
                    onValueChange = { ip = it },
                    label = { Text("IP") },
                    modifier = Modifier
                        .padding(5.dp)
                        .fillMaxWidth()
                )
                Spacer(
                    modifier = Modifier
                        .padding(5.dp)
                        .fillMaxWidth()
                )
                TextField(
                    value = port,
                    onValueChange = { ip = it },
                    label = { Text("PORT") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .padding(5.dp)
                        .fillMaxWidth()
                )

                makeButton("Перейти к работе") {
                    setContent {

                        makeMenu(ip, port.toInt())
                    }
                }


            }
        }

    }


    @Composable
    fun makeMaterialColumn(job: @Composable () -> Unit) {
        setContent {
            MaterialTheme(
                colors = DarkColors,

                ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .background(Color.Gray),
                    horizontalAlignment = Alignment.CenterHorizontally

                ) {
                    job()
                }

            }
        }
    }

    @Composable
    fun makeMenu(host: String, port: Int) {
        var status = remember { mutableStateOf("Команды") }
        var transport = Transport(host, port);
        val driver = SP811FR_Device(transport)
        status.value = "SP811FR"


        makeMaterialColumn() {
            TopAppBar(
                title = {
                    Text(status.value)
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            Thread {
                                try {

                                    driver.printText("Какой то текст")

                                } catch (e: Exception) {
                                    e.message?.let { Log.e("SP811", it) }
                                }
                            }.start()
                        }
                    ) {
                        Icon(Icons.Filled.Favorite, contentDescription = "")
                    }

                },
                actions = {
                    IconButton(onClick = {
                        Thread {
                            try {

                                driver.abortDocument()

                            } catch (e: Exception) {
                                e.message?.let { Log.e("SP811", it) }
                            }
                        }.start()
                    }) {
                        Icon(Icons.Filled.Clear, contentDescription = "Отмена")
                    }

                },
                contentColor = Color.White,

                )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {


                makeButton("Отсыпать картофан)") {
                    useNewTread() {

                        driver.initFR()
                        driver.openDocument(DocumentTypes.SALE_DOCUMENT)

                        driver.addProduct(

                            Product(
                                "00001851",
                                0,
                                100,
                                "Картошка",
                                5,
                                "012345678905"
                            )

                        )
                        driver.saleForDocOrProduct(Discount.FOR_AMOUNT, 10, "Скидка на сумму")
                        driver.addProduct(

                            Product(
                                "00001852",
                                0,
                                300,
                                "Макрошка",
                                5,
                                "04650111387864"
                            )

                        )
                        driver.saleForDocOrProduct(Discount.PERCENT, 3, "Скидка на процент")

                        driver.addProduct(Product("00001853", 0, 1, "Капуста", 5, ""))
                        driver.addProduct(Product("00001854", 0, 1, "Укроп", 5, ""))
                        driver.subTotal()
                        driver.setNDS()
                        driver.subTotal()
                        driver.payment(0, 1500, "Баблишко оплачено")
                        driver.closeDocument()


                    }
                }

                makeButton("Возврат") {
                    useNewTread() {
                        driver.initFR()
                        driver.openDocument(DocumentTypes.REFUND_DOCUMENT)
                        driver.addProduct(Product("00001854", 0, 1, "Укроп", 5, ""))
                        driver.setNDS()
                        driver.subTotal()

                        driver.subTotal()
                        driver.payment(1, 1500, "Баблишко оплачено")
                        driver.closeDocument()
                        driver.printHeader()
                    }

                }
                makeButton("Недоделанный чек") {
                    useNewTread() {
                        driver.initFR()
                        driver.openDocument(DocumentTypes.REFUND_DOCUMENT)
                        driver.addProduct(Product("00001854", 0, 1, "Укроп", 5, ""))
//                        driver.setNDS()
//                        driver.subTotal()

                    }
                }

                makeButton("X-отчет") {
                    useNewTread() {
                        driver.initFR()

                        driver.xReport()
                    }

                }
                makeButton("Z-отчет") {
                    useNewTread() {
                        driver.initFR()
                        driver.zReport()

                    }

                }
                makeButton("Проверка состояния фискального регистратора") {
                    useNewTread() {
                        driver.checkFr()

                    }

                }
                makeButton("Копия чека") {
                    useNewTread() {
                        driver.printCopyCheck()

                    }

                }
                makeButton("Закрыть чек") {
                    useNewTread() {
                        driver.closeDocument()

                    }

                }

                makeButton("Отложить чек") {
                    useNewTread() {

                        driver.holdCheck()

                    }

                }
                makeButton("Деженный ящик") {
                    useNewTread() {
                        driver.openCashDrawer()


                    }

                }
                makeButton("Внесение\\изъятие денег") {
                    useNewTread() {

                        driver.initFR()
                        driver.openDocument(DocumentTypes.DEPOSITING_DOCUMENT)
                        driver.cashInOutOperation("Купюры по 5 тысяч", 100)
                        driver.closeDocument()
                        driver.initFR()
                        driver.openDocument(DocumentTypes.WITCHDRAWAL_DOCUMENT)
                        driver.cashInOutOperation("Купюры по 5 тысяч", 5)
                        driver.closeDocument()
                    }

                }
                makeButton("Инициализация ФР") {
                    useNewTread() {
                        driver.initFR()

                    }

                }

                makeButton("Настроить печать НДС") {
                    useNewTread() {
                        driver.initFR()
                        driver.zReport()
                        // 31 --> 00011111
                        // Номер бита
                        // 0 - Печатать налоги на отчетах
                        // 1 - Печатать налоги на чеках
                        // 2 - Отключить печать нулевых налоговых сумм
                        // 3 - Зарезервировано (не используется)
                        // 4 - Округлять сумму налога после каждой позиции
                        driver.setFrParams(12, 0, "31")


                    }

                }
                makeButton("Настроить Типы оплат") {
                    useNewTread() {
                        driver.initFR()
                        driver.zReport()
                        // Название типов  платежа для печати в блоке оплаты чека, а также в отчетах
                        // «0» зарезервированное значение для тип платежа «наличные»
                        driver.setFrParams(22, 2, "НАЛИЧНЫЕ")
                        driver.setFrParams(22, 1, "БАНКОВСКАЯ КАРТА")


                    }

                }


            }
        }
    }


    @Composable
    fun makeButton(text: String, job: () -> Unit) {
        Spacer(
            modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth()
        )

        Button(
            modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth(),
            onClick = {
                job();

            },
        ) {
            Text(text = text)
        }


    }

}


fun useNewTread(job: () -> Unit) {
    Thread {
        try {

            job()

        } catch (e: Exception) {
            e.message?.let { Log.e("SP811", it) }
        }
    }.start()
}







