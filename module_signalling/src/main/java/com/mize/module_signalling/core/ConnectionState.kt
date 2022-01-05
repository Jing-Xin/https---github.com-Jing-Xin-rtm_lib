package com.mize.module_signalling.core

//链接状态
interface ConnectionState {
    companion object {
        //初始状态。SDK 未连接到系统。
        const val CONNECTION_STATE_UNKNOWN = -1

        //SDK 正在登录系统。
        const val CONNECTION_STATE_CONNECTING = 0

        //SDK 已登录系统。
        const val CONNECTION_STATE_CONNECTED = 1

        //SDK 与系统连接由于网络原因出现中断，SDK 正在尝试自动重连系统。
        const val CONNECTION_STATE_RECONNECTING = 2
        //断开连接
        const val CONNECTION_STATE_DISCONNECTED= 3

        //SDK连接取消，放弃建立连接。 同一用户在另一设备登录
        const val CONNECTION_STATE_ABORTED = 4
    }
}

interface ConnectionChangeReason {
    companion object {
        // SDK 正在登录系统。
        const val CONNECTION_CHANGE_REASON_LOGIN = 1
        //SDK 登录系统成功。
        const val CONNECTION_CHANGE_REASON_LOGIN_SUCCESS = 2
        // SDK 登录系统失败。
        const val CONNECTION_CHANGE_REASON_LOGIN_FAILURE = 3
        // SDK 无法登录系统超过 12 秒，停止登录。可能原因：用户正处于 CONNECTION_STATE_ABORTED 状态或 CONNECTION_STATE_RECONNECTING 状态。
        const val CONNECTION_CHANGE_REASON_LOGIN_TIMEOUT = 4
        // SDK 与系统的连接被中断。
        const val CONNECTION_CHANGE_REASON_INTERRUPTED = 5
        // 用户已调用 logout() 方法登出系统。
        const val CONNECTION_CHANGE_REASON_LOGOUT = 6
        //SDK 被服务器禁止登录系统
        const val CONNECTION_CHANGE_REASON_BANNED_BY_SERVER = 7
        //另一个用户正以相同的用户 ID 登陆系统。
        const val CONNECTION_CHANGE_REASON_REMOTE_LOGIN = 8
    }
}