package com.tabarkevych.places_app.domain.errors

sealed class Error {

    open class NetworkConnectionError : Error()

    open class SomethingWentWrongError : Error()

    object InternetConnectionError : Error()

}
