package checkers

class WebtitleChecker(val url: String, val expectedTitle: String, val timeout : Int) : Checker {

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
