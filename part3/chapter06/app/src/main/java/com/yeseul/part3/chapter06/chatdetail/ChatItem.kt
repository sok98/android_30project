package com.yeseul.part3.chapter06.chatdetail

data class ChatItem(
    val senderId : String,
    val message: String
) {
    constructor(): this("", "")
}
