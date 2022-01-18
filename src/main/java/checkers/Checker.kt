package checkers

abstract class Checker(val description: String) {
    abstract fun check() : CheckResult
}

