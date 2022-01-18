package rocknrollcaller.notifiers

interface Notifier {
    fun notify(message: NotificationMessage)
}