package com.yeseul.part3.chapter01

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {

    private val resultTextView: TextView by lazy {
        findViewById(R.id.resultTextView)
    }

    private val firebaseToken: TextView by lazy {
        findViewById(R.id.firebaseTokenTextView)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initFirebase()
    }

    private fun initFirebase() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if(task.isSuccessful) {
                val token = task.result
                firebaseToken.text = task.result
            }
        }
    }
}