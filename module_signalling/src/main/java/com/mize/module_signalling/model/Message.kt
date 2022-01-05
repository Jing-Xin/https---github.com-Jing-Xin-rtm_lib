package com.mize.module_signalling


class SocketMessage {
    var channelId: String = ""
    var uid: String = ""
    var tid: String = ""
    var msgType: Int = 0
    var errorMsg = ""
    var data: String = ""

//
//    constructor(uid: String, tid: String, msgType: Int, data: String) {
//        this.uid = uid
//        this.tid = tid
//        this.msgType = msgType
//        this.data = data
//    }
//
//    constructor()
//    constructor(
//        channelId: String,
//        uid: String,
//        tid: String,
//        msgType: Int,
//        errorMsg: String,
//        data: String
//    ) {
//        this.channelId = channelId
//        this.uid = uid
//        this.tid = tid
//        this.msgType = msgType
//        this.errorMsg = errorMsg
//        this.data = data
//    }
}

abstract class ZMessage
