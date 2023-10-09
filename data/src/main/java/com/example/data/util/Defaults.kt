package com.example.data.util

const val DEFAULT_INT = Int.MIN_VALUE
const val DEFAULT_LONG = Long.MIN_VALUE
const val DEFAULT_DOUBLE = -10000.1

fun Int?.orDefault(): Int = this ?: DEFAULT_INT

fun Double?.orDefault(): Double = this ?: DEFAULT_DOUBLE

fun Boolean?.orFalse(): Boolean = this ?: false