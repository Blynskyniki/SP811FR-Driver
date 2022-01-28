package ru.lad.sp_811_fr.driver.utils

import java.nio.charset.Charset

class Product(
    val articul: String,
    val nds: Number,
    val price: Number,
    val name: String,
    val count: Number,
    val gtin: String,
    val gtinType: String = "",
)
