package smu.miso.model

class TagModel {
    var tagName: String = ""   //태그 이름
    var tagType: String = ""  //태그 종류 (동아리, 대외활동, 수업 등)
    //var requestCount: Int = 0

    //add
    constructor (tagName: String, tagType: String) {
        this.tagName = tagName
        this.tagType = tagType
        //this.requestCount = requestCount
    }

}


