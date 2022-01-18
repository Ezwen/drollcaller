package rocknrollcaller.checkers


class CheckResult(val pass: Boolean, val message: String) {
    constructor(pass: Boolean) : this(pass, "")

    override fun equals(other: Any?): Boolean {
        return other is CheckResult && this.pass == other.pass && this.message == other.message
    }

    override fun hashCode(): Int {
        var result = pass.hashCode()
        result = 31 * result + message.hashCode()
        return result
    }

    fun resultAsString(): String {
        return if (pass) "PASSED" else "FAILED"
    }
}
