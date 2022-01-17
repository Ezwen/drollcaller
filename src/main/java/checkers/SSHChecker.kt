package checkers

import java.io.ByteArrayInputStream
import java.io.InputStream
import java.nio.charset.StandardCharsets
import com.jcraft.jsch.JSch
import com.jcraft.jsch.JSchException
import com.jcraft.jsch.Session


class SSHChecker(
     val host: String,
     val port: Int,
     val hostKey: String,
     val timeout: Int
) : Checker {

    override fun check(): CheckResult {
        val jsch = JSch()
        val knownHostStream: InputStream = ByteArrayInputStream(hostKey.toByteArray(StandardCharsets.UTF_8))
        jsch.setKnownHosts(knownHostStream)
        try {
            val session: Session = jsch.getSession("nobody", host, port)
            session.setPassword("fakepassword")
            session.connect(timeout)
        } catch (exception: JSchException) {
            return if (exception.message.equals("Auth fail")) {
                CheckResult(true)
            } else {
                CheckResult(false, "Unexpected error message: " + exception.message)
            }
        }
        return CheckResult(false, "Impossible, there should have been a connection error.")
    }


}
