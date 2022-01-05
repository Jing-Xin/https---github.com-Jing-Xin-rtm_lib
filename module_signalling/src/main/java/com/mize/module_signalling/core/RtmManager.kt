package com.mize.module_signalling.core

import android.content.Context
import com.mize.module_signalling.RTMClient
import kotlin.String

object RtmManager {
    fun createRtmInstance(
        context: Context,
        appKey: String,
        appSecret: String,
        eventListener: RtmClientListener
    ): RTMClient {
        return RTMClientImpl(appKey = appKey, appSecret = appSecret, clientListener = eventListener)
    }
}