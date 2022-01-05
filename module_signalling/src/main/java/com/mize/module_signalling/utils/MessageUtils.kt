package com.mize.module_signalling

import com.mize.module_signalling.core.MessageType.RTMMessageTypeChannel
import com.mize.module_signalling.core.MessageType.RTMMessageTypeJoinChannel
import com.mize.module_signalling.core.MessageType.RTMMessageTypeLeaveChannel
import com.mize.module_signalling.core.MessageType.RTMMessageTypePeerToPeer
import com.mize.module_signalling.utils.GsonUtils

object MessageUtils {
    /**
     * 创建点对点消息
     */
    fun createPeerMessage(sendId: String, toUserId: String, message: String): String {

        val socketMessage = com.mize.module_signalling.SocketMessage().run {
            uid = sendId
            tid = toUserId
            msgType = RTMMessageTypePeerToPeer
            data = message
            this
        }

        return GsonUtils.toJson(socketMessage)
    }

    /**
     * 创建加入房间消息
     */
    fun createJoinMessage(sendId: String, cId: String, message: String): String {
        val socketMessage = com.mize.module_signalling.SocketMessage().run {
            uid = sendId
            channelId = cId
            msgType = RTMMessageTypeJoinChannel
            data = message
            this
        }
        return GsonUtils.toJson(socketMessage)
    }

    /**
     * 创建离开房间消息
     */
    fun createLeaveMessage(sendId: String, cId: String, message: String): String {
        val socketMessage = com.mize.module_signalling.SocketMessage().run {
            uid = sendId
            channelId = cId
            msgType = RTMMessageTypeLeaveChannel
            data = message
            this
        }
        return GsonUtils.toJson(socketMessage)
    }

    /**+
     *创建房间内消息
     */
    fun createChannelMessage(sendId: String, cId: String, message: String): String {
        val socketMessage = com.mize.module_signalling.SocketMessage().run {
            uid = sendId
            channelId = cId
            msgType = RTMMessageTypeChannel
            data = message
            this
        }
        return GsonUtils.toJson(socketMessage)
    }
}

data class PeerMessgae(
    val uid: String,
    val tid: String,
    val message: com.mize.module_signalling.PeerMessageContent
)

data class PeerMessageContent(
    val msgType: String,
    val extraData: String
)