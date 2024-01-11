package com.tabarkevych.places_app.domain.exeptions

import java.io.IOException

data class NetworkHttpException(val code: Int, val errorMessage: String?) : IOException()