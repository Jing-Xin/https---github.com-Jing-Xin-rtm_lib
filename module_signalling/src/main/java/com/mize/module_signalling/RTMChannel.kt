package com.mize.module_signalling

import com.mize.module_signalling.core.MessageType.RTMMessageTypeChannelMembers
import com.mize.module_signalling.core.MessageType.RTMMessageTypeJoinChannel
import com.mize.module_signalling.core.MessageType.RTMMessageTypeLeaveChannel
import com.mize.module_signalling.core.SocketManager

class RTMChannel(
    val mClient: SocketManager,
    val channelId: String,
    private val uid: String,
    private var leave: () -> Unit
) {
    var members = arrayListOf<String>()
    var channelListener: RTMChannelListener? = null

    /**
     * 加入房间
     */
    fun joinChannel(callback: RTMJoinChannelCallback) {
        val msg = MessageUtils.createJoinMessage(uid, channelId, "加入房间")
        mClient.sendMessage(msg)
        callback.completion(RTMJoinChannelStatusCode.Success)
    }

    /**
     * 离开房间
     */
    fun leaveChannel(callback: RTMLeaveChannelCallback) {
        val msg = MessageUtils.createLeaveMessage(uid, channelId, "离开房间")
        mClient.sendMessage(msg)
        leave.invoke()
        callback.completion(RTMLeaveChannelStatusCode.Success)
    }

    internal fun onMessage(msg: SocketMessage) {

        when (msg.msgType) {
//            RTMMessageTypeChannel -> {
//                channelListener?.onReceiveMessage(peerId = msg.uid, msg = msg.data)
//            }
            RTMMessageTypeJoinChannel -> {
                channelListener?.onMemberJoined(msg.uid)
            }
            RTMMessageTypeLeaveChannel -> {
                channelListener?.onMemberLeaved(msg.uid)
            }
            RTMMessageTypeChannelMembers -> {
                var array = msg.data.split(",")
                members.clear()
                members.addAll(array)
            }

            else -> {
                channelListener?.onReceiveMessage(peerId = msg.uid, msg = msg.data)
            }
        }
//        if (msg.msgType == 202) {
//            val array = msg.data.split(",")
//            userChanegeListener?.invoke(array)
//            return
//        }
//        messageListener?.invoke(msg.uid, msg.data)

    }

    fun sendMessage(msg: String, callback: RTMSendChannelCallback) {
        val msg = MessageUtils.createChannelMessage(uid, channelId, msg)
        mClient.sendMessage(msg)
        callback.completion(RTMSendChannelMessageStatusCode.Success)
    }
}

//加入房间回调
interface RTMJoinChannelCallback {
    fun completion(statusCode: RTMJoinChannelStatusCode)
}

//离开房间回调
interface RTMLeaveChannelCallback {
    fun completion(statusCode: RTMLeaveChannelStatusCode)
}

//发送频道消息
interface RTMSendChannelCallback {
    fun completion(statusCode: RTMSendChannelMessageStatusCode)
}


/**
 * 离开频道状态码
 */
enum class RTMLeaveChannelStatusCode(code: Int) {
    Success(0)//成功
}

/**
 * 加入频道状态码
 */
enum class RTMJoinChannelStatusCode(code: Int) {
    Success(0)//成功
}

/**
 * 发送频道消息状态码
 */
enum class RTMSendChannelMessageStatusCode(code: Int) {
    Success(0)//成功
}

//频道相关监听
interface RTMChannelListener {
    //收到消息
    fun onReceiveMessage(peerId: String, msg: String)

    //有人加入房间
    fun onMemberJoined(peerId: String)

    //有人离开房间
    fun onMemberLeaved(peerId: String)
}
