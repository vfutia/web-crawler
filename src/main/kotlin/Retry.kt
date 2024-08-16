import kotlinx.coroutines.delay

suspend fun <T> retry(
    currentRetry: Int = 0,
    maxRetries: Int = 3,
    delayBetweenRetries: Long = 500L,
    propagateErrorAfterMaxRetries: Boolean = true,
    block: suspend () -> T?
): T? {
    return try {
        block()
    } catch (e: Exception) {
        if (currentRetry == maxRetries) {
            if (propagateErrorAfterMaxRetries) {
                throw e
            }

            null
        } else {
            delay(delayBetweenRetries)
            retry (
                currentRetry = currentRetry + 1,
                maxRetries = maxRetries,
                delayBetweenRetries = delayBetweenRetries,
                propagateErrorAfterMaxRetries = propagateErrorAfterMaxRetries,
                block = block
            )
        }
    }
}