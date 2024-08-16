package crawler.model

class ContentMatch (
    val matchedTerms: List<String>,
    val baseUrl: String,
    val fullUrl: String,
    val context: MatchContext
) {
    enum class MatchContext { TEXT, IMAGE, SOUND, VIDEO }
}