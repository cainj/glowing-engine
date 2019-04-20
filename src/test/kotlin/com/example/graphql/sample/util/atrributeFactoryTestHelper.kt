package com.example.graphql.sample.util

import com.thedeanda.lorem.LoremIpsum
import java.text.DecimalFormat
import java.util.concurrent.ThreadLocalRandom

fun getRandomInteger(lowerBound: Int = 2, upperBound: Int = 1000, exclude: Int? = null): Int {
    return getRandomLong(lowerBound.toLong(), upperBound.toLong(), exclude?.toLong()).toInt()
}

fun getRandomLong(lowerBound: Long = 2, upperBound: Long = 1000, exclude: Long? = null): Long {
    var random = exclude
    while (random == exclude) {
        random = ThreadLocalRandom.current().nextLong(lowerBound, upperBound)
    }
    return random!!
}

fun getRandomWords(count: Int = 1): String = LoremIpsum.getInstance().getWords(count)

fun getRandomURL(): String = LoremIpsum.getInstance().getUrl()

fun getRandomAmount(): Float {
    val num = ThreadLocalRandom.current().nextFloat()
    val df = DecimalFormat("#.##")

    return df.format(num).toFloat()
}
