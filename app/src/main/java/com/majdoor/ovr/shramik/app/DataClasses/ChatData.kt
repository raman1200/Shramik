package com.majdoor.ovr.shramik.app.DataClasses

class ChatData{
    lateinit var message:String
    lateinit var uid:String
    lateinit var time:String
    lateinit var date:String


    constructor()
    constructor(message: String, uid: String, time: String, date: String) {
        this.message = message
        this.uid = uid
        this.time = time
        this.date = date
    }
}