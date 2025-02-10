package io.github.zenhelix.web

public sealed class ResponseResult<out S : Any, out E : Any> {

    public data class Success<out D : Any>(val data: D) : ResponseResult<D, Nothing>()

    /** Ошибки, которые можно обрабатывать (бизнес-ошибки) */
    public data class Error<out E : Any>(val data: E? = null, val cause: Exception? = null) : ResponseResult<Nothing, E>()

    /** Внезапные ошибки во время обработки запроса/ответа, сетевые и etc */
    public data class UnexpectedError(val cause: Exception) : ResponseResult<Nothing, Nothing>()

}