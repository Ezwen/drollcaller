package drollcaller.checkers

object CheckerRunner {

    fun runChecker(checker: Checker) : CheckResult {
        return try {
            checker.check()
        } catch (e : Throwable) {
            CheckResult(false, e.message!!)
        }
    }

}