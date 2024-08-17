import crawler.Crawler
import crawler.CrawlerConfiguration
import crawler.criteria.DefaultCrawlerCriteria
import crawler.scraper.SkrapeScraper
import crawler.site.CrawlableSite
import kotlinx.coroutines.runBlocking
import org.apache.commons.validator.routines.UrlValidator

fun main(args: Array<String>) {
    try {
        processInput(args)
        startCrawl(args)
    } catch (e: Exception) {
        println(e.message)
    }
}

private fun startCrawl(args: Array<String>) {
    runBlocking {
        val site: CrawlableSite = object : CrawlableSite {
            override fun getRootUrl(): String = args[0]
            override fun getSearchUrl(terms: List<String>): String? = null
        }

        val configuration = CrawlerConfiguration.Builder()
            .startUrl(site.getRootUrl())
            .crawlerCriteria(DefaultCrawlerCriteria(site.getRootUrl()))
            .scraper(SkrapeScraper())
            .searchTerms(args.toList().subList(1, args.size))
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

private fun processInput(args: Array<String>) {
    if (args.size < 2) {
        throw IllegalArgumentException("Your command line parameters must contain both a site to crawl and at least one search term")
    }

    if (!UrlValidator().isValid(args[0])) {
        throw IllegalArgumentException("The url have you have entered is not a valid url: ${args[0]}")
    }
}