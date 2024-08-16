package crawler

import crawler.browser.DefaultHeaders
import crawler.browser.Headers
import crawler.criteria.CrawlerCriteria
import crawler.criteria.DefaultCrawlerCriteria
import crawler.matcher.ContentMatcher
import crawler.matcher.DefaultContentMatcher
import crawler.matcher.ImageContentMatcher
import crawler.model.PageContent
import crawler.scraper.Scraper

class CrawlerConfiguration(
    val startUrl: String,
    val crawlerCriteria: CrawlerCriteria = DefaultCrawlerCriteria(startUrl),
    val scraper: Scraper,
    val headers: Headers = DefaultHeaders(),
    val searchTerms: List<String>,
    val textContentMatcher: ContentMatcher = DefaultContentMatcher(),
    val imageContentMatcher: ContentMatcher = ImageContentMatcher(),
    val maxDepth: Int = DEFAULT_MAX_DEPTH,
    val minTimeToNextCrawl: Long = DEFAULT_MIN_TIME_TO_NEXT_CRAWL,
    val maxTimeToNextCrawl: Long = DEFAULT_MAX_TIME_TO_NEXT_CRAWL
) {
    companion object {
        const val DEFAULT_MAX_DEPTH = 5
        const val DEFAULT_MIN_TIME_TO_NEXT_CRAWL = 500L
        const val DEFAULT_MAX_TIME_TO_NEXT_CRAWL = 1000L
    }

    private constructor(builder: Builder) : this(
        builder.startUrl,
        builder.crawlerCriteria,
        builder.scraper,
        builder.headers,
        builder.searchTerms,
        builder.textContentMatcher,
        builder.imageContentMatcher,
        builder.maxDepth,
        builder.minTimeToNextCrawl,
        builder.maxTimeToNextCrawl
    )

    class Builder {
        var startUrl: String = ""
            private set

        var crawlerCriteria: CrawlerCriteria = DefaultCrawlerCriteria(startUrl)
            private set

        var scraper: Scraper = object : Scraper {
            override suspend fun scrapePage(pageUrl: String,
                                            requestHeaders: Headers,
                                            criteria: CrawlerCriteria): PageContent {
                throw IllegalStateException("No scaper set for configuration.  You must set a scraper")
            }
        }
            private set
        
        var headers: Headers = DefaultHeaders()
            private set

        var searchTerms: List<String> = listOf()
            private set

        var textContentMatcher: ContentMatcher = DefaultContentMatcher()
            private set

        var imageContentMatcher: ContentMatcher = ImageContentMatcher()
            private set

        var maxDepth: Int = 0
            private set

        var minTimeToNextCrawl: Long = DEFAULT_MIN_TIME_TO_NEXT_CRAWL
            private set

        var maxTimeToNextCrawl: Long = DEFAULT_MAX_TIME_TO_NEXT_CRAWL
            private set

        fun startUrl(startUrl: String) = apply { this.startUrl = startUrl }

        fun crawlerCriteria(criteria: CrawlerCriteria) = apply { this.crawlerCriteria = criteria }

        fun headers(headers: Headers) = apply { this.headers = headers }

        fun scraper(scraper: Scraper) = apply { this.scraper = scraper }

        fun searchTerms(searchTerms: List<String>) = apply { this.searchTerms = searchTerms }

        fun contentMatcher(matcher: ContentMatcher) = apply { this.textContentMatcher = textContentMatcher }

        fun maxDepth(maxDepth: Int) = apply { this.maxDepth = maxDepth }

        fun minTimeToNextCrawl(minTimeToNextCrawl: Long) = apply { this.minTimeToNextCrawl = minTimeToNextCrawl }

        fun maxTimeToNextCrawl(maxTimeToNextCrawl: Long) = apply { this.maxTimeToNextCrawl = maxTimeToNextCrawl }

        fun build() = CrawlerConfiguration(this)
    }
}