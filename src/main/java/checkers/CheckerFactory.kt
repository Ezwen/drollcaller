package checkers

import core.*

class CheckerFactory {

    fun createCheckerFromConfiguration(checkerConfiguration : Check) : Checker {
        when (checkerConfiguration) {
            is SSHCheck -> return SSHChecker()
            is SynapseCheck -> return SynapseChecker()
            is WebContentCheck -> return WebContentChecker()
            is WebtitleCheck -> return WebtitleChecker()
        }
        throw Error("INVALID CHECK CONFIGURATION")
    }
}