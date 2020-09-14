package smu.miso.model

class NotificationModel {
    var to: String? = null
    var notification = Notification()

    class Notification {
        var title: String? = null
        var body: String? = null
    }

}