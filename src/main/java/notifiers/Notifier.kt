package notifiers

import java.util.Properties
import java.lang.RuntimeException
import notifiers.NotificationMessage

interface Notifier {
    fun notify(message: NotificationMessage)
}