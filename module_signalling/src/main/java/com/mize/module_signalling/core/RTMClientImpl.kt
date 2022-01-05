package com.mize.module_signalling.core

import android.util.Log
import com.mize.module_signalling.*
import com.mize.module_signalling.core.ConnectionState.Companion.CONNECTION_STATE_ABORTED
import com.mize.module_signalling.core.ConnectionState.Companion.CONNECTION_STATE_CONNECTED
import com.mize.module_signalling.core.ConnectionState.Companion.CONNECTION_STATE_RECONNECTING
import com.mize.module_signalling.core.ConnectionState.Companion.CONNECTION_STATE_DISCONNECTED
import com.mize.module_signalling.core.ConnectionState.Companion.CONNECTION_STATE_CONNECTING
import com.mize.module_signalling.core.MessageType.RTMMessageTypeChannelMembers
import com.mize.module_signalling.core.MessageType.RTMMessageTypeKickOff
import com.mize.module_signalling.core.MessageType.RTMMessageTypeServerDisConnected
import com.mize.module_signalling.utils.GsonUtils

class RTMClientImpl(
    val appKey: String,
    val appSecret: String,
    val clientListener: RtmClientListener
) :
    RTMClient(), KEvent {
    private val TAG = "ZtmClient"
    private var verifySuccessful = true
    private var loginCallback: RTMLoginCallback? = null
    private val mClient: SocketManager = SocketManager()
    private var nowChannel: RTMChannel? = null
    var mUserId = ""

    init {
        //进行校验appid是否正确
        mClient.kEvent = this
    }

    override fun release() {

    }


    override fun loginWithToken(
        token: String,
        userId: String,
        resultCallback: RTMLoginCallback
    ) {
        if (!verifySuccessful) {
            Log.e(TAG, "appid 校验失败")
            resultCallback.completion(RTMLoginStatusCode.RTMLoginErrorInvalidAppKeyOrAppSecret)
            return
        }
        if (token.isNullOrEmpty()) {
            resultCallback.completion(RTMLoginStatusCode.RTMLoginErrorInvalidToken)
            return
        }
        mUserId = userId
        loginCallback = resultCallback
        clientListener?.onConnectionStateChanged(CONNECTION_STATE_CONNECTING)
        mClient.connect(token)
    }

    override fun logout() {
        mClient.unConnect()
    }

    override fun createChannel(channelId: String): RTMChannel {
        nowChannel = RTMChannel(mClient, channelId, mUserId) {
            nowChannel = null
        }
        return nowChannel!!
    }


    override fun sendMessageToPeer(
        peerId: String,
        message: String,
        callback: RTMSendPeerMessageCallback?
    ) {
        val msg =
            MessageUtils.createPeerMessage(mUserId, peerId, message)
        mClient.sendMessage(msg)
        callback?.completion(RTMSendPeerMessageStatusCode.RTMSendPeerMessageSuccess)
    }

    override fun onConnect() {//2843  9752
//        post {
        Log.e("zgj", "post ${Thread.currentThread().name}")
        loginCallback?.completion(RTMLoginStatusCode.RTMLoginSuccess)
        clientListener.onConnectionStateChanged(
            CONNECTION_STATE_CONNECTED
        )
//        }

    }

    override fun onDisconnect() {
        clientListener.onConnectionStateChanged(
            CONNECTION_STATE_DISCONNECTED
        )
    }

    override fun reConnect() {
        clientListener.onConnectionStateChanged(
            CONNECTION_STATE_RECONNECTING
        )
    }

    override fun onMessage(msg: String) {
//        post {
        handleMessage(msg)
//        }
    }

    private fun handleMessage(msg: String) {
        val message = GsonUtils.toBean(msg, SocketMessage::class.java)
        when (message.msgType) {
            RTMMessageTypeKickOff -> {//其他设备登录
                //断开连接
                mClient.unConnect()
                //回调通知
                clientListener.onConnectionStateChanged(CONNECTION_STATE_ABORTED)
            }
            RTMMessageTypeServerDisConnected -> {//服务端断开链接
                //重联
                mClient.reConnect()
                //回调通知
                clientListener.onConnectionStateChanged(CONNECTION_STATE_RECONNECTING)
            }
            else -> {//消息
                if (nowChannel != null && nowChannel!!.channelId == message.channelId) {//频道拦截 房间消息拦截
                    nowChannel!!.onMessage(message)
                    return
                }
                if (message.msgType == RTMMessageTypeChannelMembers) {//房间内的信令 人数变化
                    return
                }
                if (message.uid == mUserId) return
                Log.e("zgj", "post ${Thread.currentThread().name}")
                clientListener.onMessageReceived(message.data, message.uid)
            }
        }
    }

}

