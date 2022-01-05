package com.mize.module_signalling

import android.content.Context
import com.mize.module_signalling.core.RtmClientListener
import com.mize.module_signalling.core.RtmManager

abstract class RTMClient {
    companion object {
        fun createInstance(
            context: Context,
            appKey: String,
            appSecret: String,
            eventListener: RtmClientListener
        ): RTMClient {
            return RtmManager.createRtmInstance(context, appKey, appSecret, eventListener)
        }
    }

    /**
     * 释放
     */
    abstract fun release()

    /**
     * 登录
     */
    abstract fun loginWithToken(
        token: String,
        userId: String,
        resultCallback: RTMLoginCallback
    )

    /**
     * 退出
     */
    abstract fun logout()


    /**
     * 创建房间对象
     */
    abstract fun createChannel(channelId: String): RTMChannel

    /**
     * 发送消息
     */
    abstract fun sendMessageToPeer(
        peerId: String,
        message: String,
        callback:RTMSendPeerMessageCallback?
    )

}

interface RTMLoginCallback {
    fun completion(code: RTMLoginStatusCode)
}

enum class RTMLoginStatusCode(code: Int) {
    RTMLoginSuccess(0), //登录成功
    RTMLoginErrorUnknown(1),//错误未知
    RTMLoginErrorInvalidAppKeyOrAppSecret(2),//无效的Appkey 或 AppSecret
    RTMLoginErrorInvalidToken(3)// 无效的token
}

interface RTMSendPeerMessageCallback {
    fun completion(code: RTMSendPeerMessageStatusCode)
}

enum class RTMSendPeerMessageStatusCode(code: Int) {
    RTMSendPeerMessageSuccess(0)
}