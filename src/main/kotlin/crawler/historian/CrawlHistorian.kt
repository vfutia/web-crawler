package crawler.historian

class CrawlHistorian {
    private val crawlHistory: MutableMap<String, CrawlState> = mutableMapOf()

    fun crawlScheduled(url: String) { crawlHistory[url] = CrawlState.NOT_STARTED }
    fun crawlStarted(url: String) { crawlHistory[url] = CrawlState.STARTED }
    fun crawlFinished(url: String) { crawlHistory[url] = CrawlState.FINISHED }

    fun hasCrawledUrl(url: String): Boolean = crawlHistory.contains(url)

    fun urlsCrawled() = crawlHistory.mapNotNull { record ->
        if (record.value == CrawlState.FINISHED) {
            record
        } else {
            null
        }
    }

    fun urlsUncrawled() = crawlHistory.mapNotNull { record ->
        if (record.value != CrawlState.FINISHED) {
            record
        } else {
            null
        }
    }
}