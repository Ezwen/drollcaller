package rocknrollcaller.checkers

interface Checker {
    fun check(): CheckResult
    fun getDescription(): String
}

