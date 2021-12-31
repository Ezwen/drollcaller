package notifiers

import java.util.*
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class EmailNotifier(
    var host: String,
    var port: Int,
    var enableTLS: Boolean,
    var userName: String,
    var password: String,
    var towards: String
) : Notifier {
    private fun sendEmail(messageBody: String?) {
        val username = userName
        val password = password
        val props = Properties()
        props.put("mail.smtp.starttls.enable", enableTLS)
        props.put("mail.smtp.auth", "true")
        props.put("mail.smtp.host", host)
        props.put("mail.smtp.port", port)

        val session = Session.getInstance(props, object : Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(username, password)
            }
        })

        try {
            val message: Message = MimeMessage(session)
            message.setFrom(InternetAddress("noreply@mandragot.org"))
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(towards))
            message.setSubject("Monitoring Mandragot")
            message.setText(messageBody)
            Transport.send(message)
            println("Done")
        } catch (e: MessagingException) {
            throw RuntimeException(e)
        }
    }

    override fun notify(message: NotificationMessage) {
        sendEmail(message.full)
    }
}