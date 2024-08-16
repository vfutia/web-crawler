package crawler.extension

import it.skrape.selects.Doc
import java.net.URL

fun String.trimTrailingSlash(): String = removeSuffix("/")

fun String.fullUrlToSiteName(): String {
    val url = URL(this)
    return "${url.protocol}://${url.host}"
}

fun Doc.eachLinkWithAbsoluteUrls(): Map<String, String> {
    return eachLink.mapValues { entry ->
        if (entry.value.startsWith("/")) {
            "${document.baseUri()}${entry.value.trim('/')}"
        } else {
            entry.value
        }
    }
}

fun Doc.eachImageWithAbsoluteUrls(): Map<String, String> {
    return eachImage.mapValues { entry ->
        if (entry.value.startsWith("/")) {
            "${document.baseUri()}${entry.value.trim('/')}"
        } else {
            entry.value
        }
    }
}