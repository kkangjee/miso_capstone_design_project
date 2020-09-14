package smu.miso.model

class MyTagModel {
    val myTagName: String
    val myTagType: String
    val alarmOnOff: Boolean

    constructor (myTagName: String, myTagType: String, alarmOnOff: Boolean) {
        this.myTagName = myTagName
        this.myTagType = myTagType
        this.alarmOnOff = alarmOnOff

    }
}
