package smu.miso.model

//default Chat Model
//전체 학과 학생 매칭을 통한 채팅에 필요한 정보
open class ChatModel {
    private var roomId: String? = null
    private var users: ArrayList<String>? = null
    private var userCount: Int? = null
    private var messages: HashMap<String, String> = HashMap()

    class Message() {
        @JvmField
        open var uid: String = ""
        @JvmField
        open var department: String = ""
        @JvmField
        open var msg: String = ""
        @JvmField
        open var msgtype: String = ""
        @JvmField
        open var timestamp: Any = ""
        @JvmField
        open var readUsers: HashMap<String, Boolean> = HashMap()
    }

    class FileInfo {
        var filename: String? = null
        var filesize: String? = null
    }
}