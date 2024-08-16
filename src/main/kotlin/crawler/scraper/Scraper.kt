package crawler.scraper

import crawler.browser.Headers
import crawler.criteria.CrawlerCriteria
import crawler.model.PageContent

interface Scraper {
    suspend fun scrapePage(pageUrl: String, requestHeaders: Headers, criteria: CrawlerCriteria): PageContent
}