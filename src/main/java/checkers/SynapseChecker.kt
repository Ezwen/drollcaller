package checkers

class SynapseChecker : Checker {
    override fun check() : CheckResult {
        return CheckResult(false, "Hello")
    }
}
