package smu.miso.model

class UserModel() {
    private var uid: String? = null
    private var token: String? = null
    private var userid: String? = null
    private var usermsg: String? = null
    private var usernm: String? = null

    private var email: String = ""
    private var emailVerified: Boolean = false
    private var department: String = ""
    private var tags: ArrayList<String>? = null

    //회원가입 시 필요한 정보
    constructor (uid: String, email: String?, emailVerified:Boolean, department: String?, tags : ArrayList<String>?) : this()
    {
        this.uid = uid
        this.email = email.toString()
        this.emailVerified = emailVerified
        this.department = department.toString()
        this.tags = tags
    }
}