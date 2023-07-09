package com.tabarkevych.places_app.extensions

import android.util.Log
import com.tabarkevych.places_app.BuildConfig

fun Any.loge(message: String) {
    if (BuildConfig.DEBUG)
        Log.e(this::class.java.name, message)
}

fun loge(tag: String, message: String) {
    if (BuildConfig.DEBUG) {
        Log.e(tag, message)
    }
}

fun loge(tag: String, ex: Exception) {
    loge(tag, "${ex}\n${ex.stackTraceToString()}")
}

fun loge(tag: String, message: String, ex: Exception) {
    loge(tag, "$message\n${ex}\n${ex.stackTraceToString()}")
}

fun Any.loge(ex: Throwable) {
    if (BuildConfig.DEBUG)
        loge(this::class.java.name, "${ex}\n${ex.stackTraceToString()}")
}

fun loge(tag: String, text: String?, tr: Throwable?) {
    if (!BuildConfig.DEBUG) return
    Log.e(tag, text, tr)
}

inline fun logd(tag: String, message: () -> String) {
    if ( BuildConfig.DEBUG) {
        Log.d(tag, message())
    }
}

fun Any.logi(message: () -> String) {
    if (BuildConfig.DEBUG)
        Log.i(this::class.java.name, message())
}
