package drollcaller.notifiers

import java.net.URL

class FreeSMSNotifier(var smsUser: String, var smsPass: String) : Notifier {
    override fun notify(message: NotificationMessage) {
        val urlString = ("https://smsapi.free-mobile.fr/sendmsg?user=" + smsUser + "&pass=" + smsPass + "&msg="
                + message.summary)
        try {
            val url = URL(urlString)
            val conn = url.openConnection()
            conn.getInputStream()
        } catch (e: Exception) {
            throw Exception("Could not send SMS because of error: " + e.message, e)
        }
    }
}