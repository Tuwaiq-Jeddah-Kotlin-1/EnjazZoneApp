package com.tuwaiq.enjazzoneapp.notifications

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.tuwaiq.enjazzoneapp.R
//import kotlinx.android.synthetic.main.activity_result.*

class ResultActivity : AppCompatActivity() {
    private lateinit var txtTitleView:TextView
    private lateinit var txtMsgView:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        txtTitleView = findViewById(R.id.txtTitleView)
        txtTitleView = findViewById(R.id.txtMsgView)
        if (intent.getBooleanExtra("notification", false)) { //Just for confirmation
            txtTitleView.text = intent.getStringExtra("title")
            txtMsgView.text = intent.getStringExtra("message")

        }


    }
}
