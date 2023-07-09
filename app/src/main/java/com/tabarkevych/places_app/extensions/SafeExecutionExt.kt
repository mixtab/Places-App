package com.tabarkevych.places_app.extensions

import com.tabarkevych.places_app.domain.errors.handlers.ErrorHandler
import com.tabarkevych.places_app.domain.errors.handlers.IErrorHandler
import kotlinx.coroutines.flow.Flow
import com.tabarkevych.places_app.domain.base.Result
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

suspend fun <T : Any> Any.executeSafeWithResultAsync(
    errorHandler: IErrorHandler = ErrorHandler,
    call: suspend () -> Result<T>
): Result<T> {
    return try {
        call()
    } catch (e: Exception) {
        loge(e)
        Result.Failure(errorHandler.getError(e))
    }
}

inline fun <T : Any> Any.executeSafeWithResult(
    errorHandler: IErrorHandler = ErrorHandler,
    call: () -> Result<T>
): Result<T> {
    return try {
        call()
    } catch (e: Exception) {
        loge(e)
        Result.Failure(errorHandler.getError(e))
    }
}

inline fun <T> Any.executeSafeWithKotlinResult(
    errorHandler: IErrorHandler = ErrorHandler,
    call: () -> kotlin.Result<T>
): kotlin.Result<T> {
    return try {
        call()
    } catch (e: Exception) {
        loge(e)
        kotlin.Result.failure(e)
    }
}


/**
 * Executes the [call] block and returns the result of call or null if any exception occurred
 * */
inline fun <T : Any?> Any.executeSafeOrNull(
    call: () -> T
): T? {
    return try {
        call()
    } catch (e: Throwable) {
        loge(e)
        null
    }
}


fun < T : Any> Flow<Result<T>>.safe(errorHandler: IErrorHandler = ErrorHandler): Flow<Result<T>> {
    return catch { ex ->
        loge(ex.stackTraceToString())
        emit(Result.Failure(errorHandler.getError(ex)))
    }
}

fun < T : Any> Flow<T>.safeResult(errorHandler: IErrorHandler = ErrorHandler): Flow<Result<T>> {
    return map {
        Result.Success(it)
    }.safe(errorHandler)
}
