package notifiers

import java.util.Properties
import java.lang.RuntimeException
import notifiers.NotificationMessage

class NotificationMessage(val title: String, val summary: String, val full: String)