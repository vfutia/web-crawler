package crawler.model

data class PageContent (
    val url: String,
    val statusCode: Int,
    val statusMessage: String,
    val links: List<String> = listOf(),
    val images: List<String> = listOf(),
    val text: String
)