package drollcaller.notifiers

interface Notifier {
    fun notify(message: NotificationMessage)
}