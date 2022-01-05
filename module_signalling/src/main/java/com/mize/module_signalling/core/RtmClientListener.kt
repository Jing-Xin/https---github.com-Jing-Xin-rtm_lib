package com.mize.module_signalling.core


interface RtmClientListener {

    //SDK 与系统的连接状态发生改变回调。
    fun onConnectionStateChanged(state: Int)

    //收到点对点消息回调。
    // message	被接收的消息。详见 ZtmMessage。
    //peerId	消息发送者的用户 ID。
    fun onMessageReceived(message: String, peerId: String)

    //收到点对点图片消息回调。
    //message	被接收的消息。详见 RtmImageMessage。
    //peerId	消息发送者的用户 ID。
//    fun onImageMessageReceivedFromPeer(message: RtmImageMessage?, peerId: String?)

    //收到点对点文件消息回调。
    //message	被接收的消息。详见 RtmFileMessage。
    //peerId	消息发送者的用户 ID。
//    fun onFileMessageReceivedFromPeer(var1: RtmFileMessage?, var2: String?)

    //主动回调：上传进度回调。
    //progress	文件或图片的上传进度。详见 RtmMediaOperationProgress。
    //requestId	标识本次上传请求的的唯一 ID。
//    fun onMediaUploadingProgress(var1: RtmMediaOperationProgress?, var2: Long)

    //主动回调：下载进度回调。
    //progress	文件或图片的下载进度。详见 RtmMediaOperationProgress。
    //requestId	标识本次下载请求的的唯一 ID。
//    fun onMediaDownloadingProgress(var1: RtmMediaOperationProgress?, var2: Long)

    //（SDK 断线重连时触发）当前使用的 RTM Token 已超过 24 小时的签发有效期。
    //该回调仅会在 SDK 处于 CONNECTION_STATE_RECONNECTING 状态时因 RTM 后台监测到 Token 签发有效期过期而触发。
    // SDK 处于 CONNECTION_STATE_CONNECTED 状态时该回调不会被触发。
    // 收到该回调时，请尽快在你的业务服务端生成新的 Token 并调用 renewToken 方法把新的 Token 传给 Token 验证服务器。
    fun onTokenExpired()

    //被订阅用户在线状态改变回调。
    //
    //首次订阅在线状态成功时，SDK 也会返回本回调，显示所有被订阅用户的在线状态。
    //每当被订阅用户的在线状态发生改变，SDK 都会通过该回调通知订阅方。
    //如果 SDK 在断线重连过程中有被订阅用户的在线状态发生改变，SDK 会在重连成功时通过该回调通知订阅方。
    //参数
    fun onPeersOnlineStatusChanged(var1: Map<String, Int>)
}

