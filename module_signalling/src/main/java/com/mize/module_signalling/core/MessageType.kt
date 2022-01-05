package com.mize.module_signalling.core

object MessageType {


    const val RTMMessageTypeDefault: Int = 0  //默认类型,频道消息不需要指定类型
    const val RTMMessageTypeHeartbeat: Int = 2  //心跳消息
    const val RTMMessageTypeKickOff: Int = 21  //被踢下线，用户在另一设备登录
    const val RTMMessageTypeServerDisConnected: Int = 22  //服务器断开连接
    const val RTMMessageTypePeerToPeer: Int = 200  //点对点消息
    const val RTMMessageTypeJoinChannel: Int = 201  //加入房间/频道
    const val RTMMessageTypeChannelMembers: Int = 202  //房间/频道人数变化回调
    const val RTMMessageTypeLeaveChannel: Int = 203  //退出房间/频道
    const val RTMMessageTypeChannel: Int = 206  //频道内消息
}
