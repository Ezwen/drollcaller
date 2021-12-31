package checkers

class WebtitleChecker : Checker {
    override fun check() : CheckResult {
        return CheckResult(false, "Hello")
    }
}
