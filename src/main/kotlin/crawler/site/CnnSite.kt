package crawler.site

import java.net.URLEncoder
import java.nio.charset.Charset

class CnnSite : CrawlableSite {
    override fun getRootUrl(): String = "https://cnn.com"
    override fun getSearchUrl(terms: List<String>): String? =
        "${getRootUrl()}/search?q=${URLEncoder.encode(terms.joinToString("+"), Charset.defaultCharset())}"
}