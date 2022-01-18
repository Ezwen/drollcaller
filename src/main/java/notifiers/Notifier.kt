package notifiers

interface Notifier {
    fun notify(message: NotificationMessage)
}