package com.example.bookshelf.ui.create

import android.os.Bundle
import android.text.InputType
import android.text.InputType.TYPE_DATETIME_VARIATION_DATE
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.bookshelf.R


class CreateActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val ei = findViewById<View>(R.id.editIssuedDate) as EditText
        ei.inputType = InputType.TYPE_CLASS_DATETIME or TYPE_DATETIME_VARIATION_DATE



    }
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            android.R.id.home->{
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}