package crawler.criteria

interface CrawlerCriteria {
    fun getCrawlableUrls(urls: List<String>): List<String>
}