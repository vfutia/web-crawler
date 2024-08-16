package crawler.browser

private const val DEFAULT_USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/127.0.0.0 Safari/537.36"
private const val DEFAULT_ACCEPT = "text/html"
private const val DEFAULT_ACCEPT_LANGUAGE = "en-US,en;q=0.9,ja;q=0.8"
private const val DEFAULT_VIEWPORT_WIDTH = "1435"

class DefaultHeaders (
    private val userAgent: String = DEFAULT_USER_AGENT,
    private val accept: String = DEFAULT_ACCEPT,
    private val acceptLanguage: String = DEFAULT_ACCEPT_LANGUAGE,
    private val viewportWidth: String = DEFAULT_VIEWPORT_WIDTH
) : Headers {
    override fun getHeaders() = mapOf(
        "User-Agent" to userAgent,
        "Accept" to accept,
        "Accept-Language" to acceptLanguage,
        "Viewport-Width" to viewportWidth
    )
}