package smu.miso.model

class UserModel() {

    var studentId: String = ""
    var uid: String = ""
    var emailVerified: Boolean = false
    var department: String = ""
    var tags: ArrayList<String>? = null
    var randomRoomId: String = ""
    var token : String = ""

    private var userPhoto: String = ""
    private var roodId: String? = null


    constructor (
        uid: String,
        studentId: String,
        emailVerified: Boolean,
        department: String?,
        tags: ArrayList<String>?

    ) : this() {
        this.uid = uid
        this.studentId = studentId
        this.emailVerified = emailVerified
        this.department = department.toString()
        this.tags = tags
    }

    //회원가입 시 필요한 정보
    constructor (
        uid: String,
        studentId: String,
        emailVerified: Boolean,
        department: String?,
        tags: ArrayList<String>?,
        randomRoomId: String?
    ) : this() {
        this.uid = uid
        this.studentId = studentId
        this.emailVerified = emailVerified
        this.department = department.toString()
        this.tags = tags
        this.randomRoomId = randomRoomId.toString()
    }
}