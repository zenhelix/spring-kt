package io.github.zenhelix.web.http.client.netty.reactor

import io.netty.channel.ChannelException
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.timeout.ReadTimeoutHandler
import java.util.concurrent.TimeUnit

public class ReadTimeoutHandler(private val timeout: Long, private val unit: TimeUnit) : ReadTimeoutHandler(timeout, unit) {
    private var closed = false

    override fun readTimedOut(ctx: ChannelHandlerContext) {
        if (!closed) {
            ctx.fireExceptionCaught(ReadTimeoutException(timeout, unit))
            ctx.close()
            closed = true
        }
    }
}

public class ReadTimeoutException(timeout: Long, unit: TimeUnit) : ChannelException("Read timeout after $timeout $unit", null, true) {
    // Suppress a warning since the method doesn't need synchronization
    override fun fillInStackTrace(): Throwable {
        return this
    }
}

