package checkers

class WebtitleChecker(private val url: String, private val expectedTitle: String, private val timeout : Int) : Checker {

    override fun check(): CheckResult {
        val driver = SilentHtmlUnitDriver(false, timeout)
        driver.get(url)
        val title = driver.title
        driver.close()
        return if (title.contains(expectedTitle)) {
            CheckResult(true)
        } else {
            CheckResult(false, "The title of the web page is not the one expected: $title")
        }
    }
}
