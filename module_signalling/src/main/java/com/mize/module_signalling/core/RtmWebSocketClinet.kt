package com.mize.module_signalling.core

import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.lang.Exception
import java.net.URI
import kotlin.String

class RtmWebSocketClinet(serverUri: URI, val iEvent: IEvent) : WebSocketClient(serverUri) {
    private val TAG = "ZtmWebSocketClinet"
    var connectFlag = false
    private val HEARTBEAT_MESSAGE_TAG = 1
    private val RESTART_CONNECT_TAG = 2
    private val HEARTBEAT_MESSAGE = "2"

    init {

        if (Looper.myLooper() == null) {
            Looper.prepare();
        }
    }

    override fun onOpen(handshakedata: ServerHandshake) {
        Log.e(TAG, "onOpen")
        connectFlag = true
        handler.post {
            iEvent.onCennect()
        }

        //开始心跳
        handler.sendEmptyMessage(HEARTBEAT_MESSAGE_TAG)
    }

    override fun onMessage(message: String) {
        if (message == HEARTBEAT_MESSAGE) {
            Log.d(TAG, "收到心跳消息")
            return
        }
        Log.e(TAG, "onMessage$message")
        handler.post {
            iEvent.onMessgae(message)
        }
    }

    override fun onClose(code: Int, reason: String, remote: Boolean) {
        Log.e("dds_error", "onClose:" + reason + "remote:" + remote)
        if (connectFlag) {
            restartConnect()
            handler.post {
                iEvent.reConnect()
            }
        } else {
            handler.post {
                iEvent.onDisconnect()
            }
        }

    }

    override fun onError(ex: Exception) {
        Log.e("dds_error", "onError${ex.message}")
    }

    private val handler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun dispatchMessage(msg: Message) {
            super.dispatchMessage(msg)
            if (msg.what == HEARTBEAT_MESSAGE_TAG) { //心跳
                if (isOpen) {
                    Log.d(TAG, "发送心跳")
                    this@RtmWebSocketClinet.sendMessage(HEARTBEAT_MESSAGE)
                    sendEmptyMessageDelayed(HEARTBEAT_MESSAGE_TAG, 10000)
                }
            } else if (msg.what == RESTART_CONNECT_TAG) { //重连
                Log.d(TAG, "重新连接")
                reconnect()
            }
        }
    }

    /**
     * 发送消息
     *
     * @param msg
     */
    private fun sendMessage(msg: String) {
        if (connection.isOpen && !handler.hasMessages(HEARTBEAT_MESSAGE_TAG)) {
            send(msg)
        }
    }

    /**
     * 重新连接
     */
    private fun restartConnect() {
        if (!handler.hasMessages(RESTART_CONNECT_TAG)) {
            //两秒后重新链接
            handler.sendEmptyMessageDelayed(RESTART_CONNECT_TAG, 5000)
        }
    }

}