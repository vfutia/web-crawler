package crawler

import crawler.extension.fullUrlToSiteName
import crawler.historian.CrawlHistorian
import crawler.model.ContentMatch
import crawler.model.PageContent
import kotlinx.coroutines.delay
import retry

class Crawler(
    private val configuration: CrawlerConfiguration
) {
    private val contentMatched: MutableMap<String, List<ContentMatch>> = mutableMapOf()
    private val historian: CrawlHistorian = CrawlHistorian()

    suspend fun start() = crawl(configuration.startUrl, currentDepth = 0)

    private suspend fun crawl(url: String, currentDepth: Int): Map<String, List<ContentMatch>> {
        if (currentDepth < configuration.maxDepth) {
            val urlToProcess = url ?: configuration.startUrl
            historian.crawlScheduled(urlToProcess)
            val urlsInPage = processPage(urlToProcess)

            urlsInPage.forEach { url ->
                crawl(url, currentDepth + 1)
            }
        }

        return contentMatched
    }

    private suspend fun processPage(url: String): List<String> {
        return try {
            retry {
                println("Crawling page: $url")
                val waitTime = generateWaitTime()
                delay(waitTime)

                val content = getPageContent(url)

                if (content.statusCode == 200) {
                    processPageForMatches(url, content)
                } else {
                    println("An error occurred processing page $url -- ${content.statusCode} ${content.statusMessage}")
                }

                println("Crawling complete for $url")

                content.links
            } ?: listOf()
        } catch (e: Exception) {
            println("Failed to process $url: ${e.message}")
            listOf()
        }
    }

    private fun processPageForMatches(url: String, content: PageContent) {
        println("Processing $url for matches")
        historian.crawlScheduled(url)

        var matches: MutableList<ContentMatch> = mutableListOf()

        //Check text on page
        configuration.textContentMatcher.matchesContent(configuration.searchTerms, content).forEach { entry ->
            matches.add(ContentMatch(entry.value, url.fullUrlToSiteName(), url, ContentMatch.MatchContext.TEXT))
        }

        //Process images for contained text
        configuration.imageContentMatcher.matchesContent(configuration.searchTerms, content).forEach { entry ->
            matches.add(ContentMatch(entry.value, url.fullUrlToSiteName(), url, ContentMatch.MatchContext.IMAGE))
        }

        contentMatched[url] = matches

        historian.hasCrawledUrl(url)
    }

    private suspend fun getPageContent(pageUrl: String): PageContent =
        configuration.scraper.scrapePage(pageUrl, configuration.headers, configuration.crawlerCriteria)

    private fun generateWaitTime(): Long = Math.round(
        configuration.minTimeToNextCrawl + (Math.random() * (configuration.maxTimeToNextCrawl - configuration.minTimeToNextCrawl)))
}