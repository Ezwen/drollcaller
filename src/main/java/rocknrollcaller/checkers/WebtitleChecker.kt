package rocknrollcaller.checkers

import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class WebtitleChecker(
    val descript: String,
    val url: String, val expectedTitle: String, val timeout: Int
) : Checker {

    override fun check(): CheckResult {
        val doc: Document = Jsoup.connect(url).timeout(timeout).ignoreContentType(true).get()
        val title = doc.title()
        return if (title.contains(expectedTitle)) {
            CheckResult(true)
        } else {
            CheckResult(false, "The title of the web page is not the one expected: $title")
        }
    }
    override fun getDescription(): String {
        return descript
    }
}
