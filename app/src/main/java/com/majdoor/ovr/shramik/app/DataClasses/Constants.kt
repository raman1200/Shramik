package com.majdoor.ovr.shramik.app.DataClasses

import android.widget.Button
import com.majdoor.ovr.shramik.app.R
import java.text.SimpleDateFormat
import java.util.*

class Constants {
    companion object {
        val SENDER_ROOM = "Sender Room"
        val RECEIVER_ROOM = "Receiver Room"
        val CHATS = "Chats"
        val POST_ID = "Post ID"
        val NAME: String = "Name"
        val IMAGE: String = "Image"
        val SAVED = "saved"
        val AVAILABLE_JOBS = "Available Jobs"
        val AVAILABLE_WORKERS = "Available Workers"
        val POSTS = "Posts"
        val TITLE = "TITLE"
        val DESCRIPTION = "DESCRIPTION"
        val CATEGORY = "CATEGORY"
        val SALARY = "SALARY"
        val LOCATION = "LOCATION"
        val DATE = "DATE"
        val UPLOADEDBY = "UPLOADER"
        val USERS = "Users"
        val BUILDER = "Builder"
        val WORKER = "Worker"
        val APPLIED_FOR = "Applied For"
        val SHARED_PREF_NAME = "my-pref"
        val SAVED_POSTS = "Saved Posts"

        fun setCurrentDate(): String {
            val calForDate = Calendar.getInstance().time
            val currentDate = SimpleDateFormat("dd-MM-yy")
            return currentDate.format(calForDate)
        }

        fun markButtonDisable(button: Button) {
            button.isEnabled = false
            button.setBackgroundResource(R.drawable.button_bg)
        }

        fun markButtonEnable(button: Button) {
            button.isEnabled = true
            button.setBackgroundResource(R.drawable.next_btn)
        }

        fun category(type: String): String {
            if (type.equals(null))
                return "null"
            else if (type.equals(BUILDER)) {
                return AVAILABLE_WORKERS
            } else if (type.equals(WORKER)) {
                return AVAILABLE_JOBS
            }
            return "null"
        }
    }
}

