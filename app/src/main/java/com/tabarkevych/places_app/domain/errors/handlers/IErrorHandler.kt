package com.tabarkevych.places_app.domain.errors.handlers

import com.tabarkevych.places_app.domain.errors.Error

interface IErrorHandler {

    fun getError(tr: Throwable): Error
}