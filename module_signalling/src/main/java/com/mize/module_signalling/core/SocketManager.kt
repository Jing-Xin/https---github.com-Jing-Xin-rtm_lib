package com.mize.module_signalling.core

import java.net.URI
import kotlin.String

class SocketManager : IEvent {
    private val SOCKET_URL = "ws://192.168.1.6:8001/signal"
    private var webSocket: RtmWebSocketClinet? = null
    var kEvent: KEvent? = null

    //    private var
    fun connect(token: String) {
        if (webSocket == null || !webSocket!!.isOpen) {
            val uri = URI(SOCKET_URL)
            webSocket = RtmWebSocketClinet(uri, this)
            webSocket!!.let {
                it.addHeader("Sec-WebSocket-Protocol", token)
//                it.addHeader("appKey", "appKey")
//                it.addHeader("appSecret", "appSecret")
                it.connect()
            }
        }
    }

    fun sendMessage(msg: String) {
        webSocket?.let {
            it.send(msg)
        }
    }

    fun unConnect() {
        webSocket?.let {
            it.connectFlag = false
            it.close()
            webSocket = null
        }
    }

    override fun onCennect() {
        kEvent?.onConnect()
    }

    override fun onDisconnect() {
        kEvent?.onDisconnect()
    }

    override fun onMessgae(msg: String) {
        kEvent?.onMessage(msg)
    }

    override fun reConnect() {
        kEvent?.reConnect()
    }

}