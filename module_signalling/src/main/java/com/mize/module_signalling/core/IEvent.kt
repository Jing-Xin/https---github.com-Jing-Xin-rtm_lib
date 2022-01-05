package com.mize.module_signalling.core

import kotlin.String

interface IEvent {
    fun onCennect()
    fun onDisconnect()
    fun onMessgae(msg: String)
    fun reConnect()
}