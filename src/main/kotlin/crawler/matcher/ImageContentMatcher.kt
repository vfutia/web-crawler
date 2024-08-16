package crawler.matcher

import crawler.model.PageContent
import org.apache.tika.metadata.Metadata
import org.apache.tika.parser.AutoDetectParser
import org.apache.tika.parser.ParseContext
import org.apache.tika.sax.BodyContentHandler
import java.io.BufferedInputStream
import java.net.URL
import java.util.regex.Matcher
import java.util.regex.Pattern


class ImageContentMatcher : ContentMatcher {
    override fun matchesContent(searchTerms: List<String>, content: PageContent): Map<String, List<String>> {
        val matches: MutableMap<String, List<String>> = mutableMapOf()

        content.images.forEach { imageUrl ->
            val stream = BufferedInputStream(URL(imageUrl).openStream())

            val parser = AutoDetectParser()
            val handler = BodyContentHandler()
            val metadata = Metadata()
            val context = ParseContext()

            parser.parse(
                stream,
                handler,
                metadata,
                context
            )

            val imageText = context.toString()

            searchTerms.forEach { term ->
                val p: Pattern = Pattern.compile("(^|\\s)$term\\b", Pattern.CASE_INSENSITIVE)
                val m: Matcher = p.matcher(imageText)

                if (m.find()) {
                    val updatedMatches = (matches[imageUrl]?.toMutableSet() ?: mutableSetOf()).apply {
                        add(term)
                    }
                    matches[imageUrl] = updatedMatches.toList()
                }
            }
        }

        return matches
    }
}
