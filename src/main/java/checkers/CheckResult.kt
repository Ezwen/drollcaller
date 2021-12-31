package checkers

class CheckResult(val pass: Boolean, val message: String) {
    constructor(pass: Boolean) : this(pass, "")
}
