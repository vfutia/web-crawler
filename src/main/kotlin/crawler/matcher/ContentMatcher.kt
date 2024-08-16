package crawler.matcher

import crawler.model.PageContent

interface ContentMatcher {
    fun matchesContent(searchTerms: List<String>, content: PageContent): Map<String, List<String>>
}