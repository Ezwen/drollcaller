package rocknrollcaller.checkers

import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class WebContentChecker(
    val descript: String,
    val url: String, val expectedContent: String, val timeout: Int
) : Checker {

    override fun check(): CheckResult {
        val doc: Document = Jsoup.connect(url).timeout(timeout).ignoreHttpErrors(true).ignoreContentType(true).get()
        val content = doc.body().text().trim { it <= ' ' }
        return if (content == expectedContent) {
            CheckResult(true)
        } else {
            CheckResult(false, "The content of the web page is not the one expected: $content")
        }
    }
    override fun getDescription(): String {
        return descript
    }
}
