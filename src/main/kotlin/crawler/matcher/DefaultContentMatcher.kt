package crawler.matcher

import crawler.model.PageContent
import java.util.regex.Matcher
import java.util.regex.Pattern

class DefaultContentMatcher : ContentMatcher {

    //The default implementation just uses a simple text based search to find instances of the
    //search terms.  Classes inheriting from this, should use their own unique, more sophisticated
    //solution.
    override fun matchesContent(searchTerms: List<String>, content: PageContent): Map<String, List<String>> {
        val matches: MutableMap<String, List<String>> = mutableMapOf()

        searchTerms.forEach { term ->
            val p: Pattern = Pattern.compile("(^|\\s)$term\\b", Pattern.CASE_INSENSITIVE)
            val m: Matcher = p.matcher(content.text)

            if (m.find()) {
                val updatedMatches = (matches[content.url]?.toMutableSet() ?: mutableSetOf()).apply {
                    add(term)
                }
                matches[content.url] = updatedMatches.toList()
            }
        }

        return matches
    }
}