package smu.miso.model

class UserModel() {

    private var studentId: String? = null
    private var uid: String? = null
    private var emailVerified: Boolean = false
    private var department: String = ""
    private var tags: ArrayList<String>? = null

    private var userPhoto: String = ""
    private var roodId: String? = null

    //회원가입 시 필요한 정보
    constructor (uid: String, studentId: String, emailVerified:Boolean, department: String?, tags : ArrayList<String>?) : this()
    {
        this.uid = uid
        this.studentId = studentId
        this.emailVerified = emailVerified
        this.department = department.toString()
        this.tags = tags
    }
}