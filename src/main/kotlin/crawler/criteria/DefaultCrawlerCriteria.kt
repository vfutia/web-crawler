package crawler.criteria

import java.net.URL

class DefaultCrawlerCriteria(
    rootUrl: String,
    private val crawlExternalDomains: Boolean = false
) : AbstractCrawlerCriteria(rootUrl) {

    override fun getCrawlableUrls(urls: List<String>): List<String> {
        return urls.mapNotNull { link ->
            if (isAllowedDomain(link)) {
                link
            } else {
                null
            }
        }
    }

    private fun isAllowedDomain(url: String): Boolean {
        return try {
            val urlToCrawl = URL(url)

            if (!crawlExternalDomains) {
                val crawlStartUrl = URL(rootUrl)
                val currentDomain = urlToCrawl.host
                val origDomain = crawlStartUrl.host

                currentDomain == origDomain
            } else {
                true
            }
        } catch (e: Exception) {
            false
        }
    }
}