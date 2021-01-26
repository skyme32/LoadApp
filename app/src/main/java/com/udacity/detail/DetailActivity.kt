package com.udacity.detail

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.udacity.MainActivity
import com.udacity.MainActivity.Companion.EXTRA_MESSAGE
import com.udacity.MainActivity.Companion.EXTRA_MESSAGE_STATE
import com.udacity.R
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)


        txt_file_name_desc.text = intent.getStringExtra(EXTRA_MESSAGE)

        val status = intent.getBooleanExtra(EXTRA_MESSAGE_STATE, true)
        if (status) {
            img_dowload_state.setColorFilter(getColor(R.color.success))
            txt_desc_status.setTextColor(getColor(R.color.success))
            txt_desc_status.text = getString(R.string.txt_success)
        } else {
            img_dowload_state.setColorFilter(getColor(R.color.fail))
            txt_desc_status.setTextColor(getColor(R.color.fail))
            txt_desc_status.text = getString(R.string.txt_fail)
        }

        btn_ok_detail.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

}
