import crawler.Crawler
import crawler.CrawlerConfiguration
import crawler.criteria.DefaultCrawlerCriteria
import crawler.scraper.SkrapeScraper
import crawler.site.CnnSite
import crawler.site.CrawlableSite
import kotlinx.coroutines.runBlocking

fun main(args: Array<String>) {
    runBlocking {
        val site: CrawlableSite = CnnSite()

        val configuration = CrawlerConfiguration.Builder()
            .startUrl(site.getSearchUrl(args.toList()) ?: site.getRootUrl())
            .crawlerCriteria(DefaultCrawlerCriteria(site.getRootUrl()))
            .scraper(SkrapeScraper())
            .searchTerms(args.toList())
            .maxDepth(2)
            .build()

        val matches = Crawler(configuration).start()

        if (matches.isEmpty()) {
            println("You're in the clear!!  No matches found!!")
        } else {
            println("Uh oh!!  We found matches for your content in the following places: ")
            println("-------------------------------------------------------------")
            matches.forEach { entry ->
                if (entry.value.isNotEmpty()) {
                    println(entry.key)
                    println("-------------------------------------------------------------")

                    entry.value.forEach { match ->
                        println("Search terms matched: ${match.matchedTerms.joinToString(", ")}")
                        println("Match type: ${match.context}")
                    }

                    println()
                    println()
                }
            }
        }
    }
}