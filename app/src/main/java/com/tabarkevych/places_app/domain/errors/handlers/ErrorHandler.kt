package com.tabarkevych.places_app.domain.errors.handlers

import com.tabarkevych.places_app.exeptions.NetworkHttpException
import com.tabarkevych.places_app.exeptions.SomethingWentWrongException
import java.net.UnknownHostException
import com.tabarkevych.places_app.domain.errors.Error

object ErrorHandler : IErrorHandler {

    override fun getError(tr: Throwable): Error =
        when (tr) {
            is SomethingWentWrongException -> {
                Error.SomethingWentWrongError()
            }
            is NetworkHttpException -> {
                when (tr.code) {
                    else -> Error.SomethingWentWrongError()
                }
            }
            is UnknownHostException ->{
                Error.InternetConnectionError
            }
            else -> Error.SomethingWentWrongError()

        }
}
