package checkers

class WebtitleChecker(
    description: String,
    val url: String, val expectedTitle: String, val timeout: Int
) : Checker(description) {

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
