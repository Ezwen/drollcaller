package notifiers

import java.util.*
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class EmailNotifier(
    private val host: String,
    private val port: Int,
    private val enableTLS: Boolean,
    private val userName: String,
    private val password: String,
    private val towards: String,
    private val from: String,
) : Notifier {
    private fun sendEmail(title: String, messageBody: String) {
        val username = userName
        val password = password
        val props = Properties()
        props["mail.smtp.starttls.enable"] = enableTLS
        props["mail.smtp.auth"] = "true"
        props["mail.smtp.host"] = host
        props["mail.smtp.port"] = port

        val session = Session.getInstance(props, object : Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(username, password)
            }
        })

        try {
            val message: Message = MimeMessage(session)
            message.setFrom(InternetAddress(from))
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(towards))
            message.setSubject(title)
            message.setText(messageBody)
            Transport.send(message)
            println("Done")
        } catch (e: MessagingException) {
            throw RuntimeException(e)
        }
    }

    override fun notify(message: NotificationMessage) {
        sendEmail("Monitoring: " + message.title, message.full)
    }
}