package checkers

class WebContentChecker : Checker {
    override fun check() : CheckResult {
        return CheckResult(false, "Hello")
    }
}
