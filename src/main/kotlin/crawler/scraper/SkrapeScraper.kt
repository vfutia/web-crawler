package crawler.scraper

import crawler.browser.Headers
import crawler.criteria.CrawlerCriteria
import crawler.extension.eachImageWithAbsoluteUrls
import crawler.extension.eachLinkWithAbsoluteUrls
import crawler.model.PageContent
import it.skrape.core.htmlDocument
import it.skrape.fetcher.AsyncFetcher
import it.skrape.fetcher.response
import it.skrape.fetcher.skrape

class SkrapeScraper : Scraper {
    override suspend fun scrapePage(
        pageUrl: String,
        requestHeaders: Headers,
        criteria: CrawlerCriteria
    ): PageContent {
        return skrape(AsyncFetcher) {
            request {
                url = pageUrl
                headers = requestHeaders.getHeaders()
            }

            response {
                val links = htmlDocument { eachLinkWithAbsoluteUrls() }.map { entry -> entry.value }
                val images = htmlDocument { eachImageWithAbsoluteUrls() }.map { entry -> entry.value }

                PageContent(
                    url = htmlDocument { document.baseUri() },
                    statusCode = status { code },
                    statusMessage = status { message },
                    links = criteria.getCrawlableUrls(links),
                    images = images,
                    text = htmlDocument { wholeText }
                )
            }
        }
    }
}