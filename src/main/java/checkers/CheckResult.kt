package checkers

import configuration.Check

class CheckResult(val pass: Boolean, val message: String) {
    constructor(pass: Boolean) : this(pass, "")

    override fun equals(other: Any?): Boolean {
        return other is CheckResult && this.pass  == other.pass && this.message == other.message
    }
}
