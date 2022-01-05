package com.mize.module_signalling.core

import kotlin.String

interface KEvent {
    fun onConnect()
    fun onDisconnect()
    fun reConnect()
    fun onMessage(msg: String)
}