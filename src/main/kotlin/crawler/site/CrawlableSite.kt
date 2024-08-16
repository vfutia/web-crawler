package crawler.site

interface CrawlableSite {
    fun getRootUrl(): String
    fun getSearchUrl(terms: List<String>): String?
}