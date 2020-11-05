package com.example.bookshelf.ui.create

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.ContentValues
import android.icu.text.Collator.getInstance
import android.os.Bundle
import android.provider.SyncStateContract.Helpers.insert
import android.text.InputType
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.bookshelf.R
import kotlinx.android.synthetic.main.activity_create.*
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Bitmap
import android.util.Log
import com.example.bookshelf.Database.*
import java.io.ByteArrayOutputStream

class CreateActivity : AppCompatActivity() {
    var setYear = 2020
    var setMonth = 1 - 1
    var setDay = 1

    // ジャンル
    val genreList = arrayOf("漫画", "雑誌", "小説")
    val ratingList = arrayOf("全年齢対象", "春画", "R-18", "R-18G")
    val possessionList = arrayOf("未所持", "デジタル", "実物", "両方")

    //版権
    private var copyright = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //発行日
        var date = findViewById<EditText>(R.id.editIssuedDate)
        date.isFocusable = false
        date.setOnClickListener {
            showDatePicker()
        }

        //ページ数
        editPage.inputType = InputType.TYPE_CLASS_NUMBER

        //作品ジャンル
        var genre = findViewById<EditText>(R.id.editGenre)
        genre.isFocusable = false
        genre.setOnClickListener {
            dialogG()
        }

        //版権
        checkBoxCopyright.inputType = InputType.TYPE_NULL
        checkBoxCopyright.setOnClickListener(View.OnClickListener
        {
            val body = findViewById<View?>(R.id.editCopyright) as EditText
            if (copyright == false) {
                copyright = true
                body.setText("オリジナル", TextView.BufferType.NORMAL)
            } else {
                copyright = false
                body.setText("二次創作", TextView.BufferType.NORMAL)
            }
        })

        //対象年齢
        var rating = findViewById<EditText>(R.id.editRating)
        rating.isFocusable = false
        rating.setOnClickListener {
            dialogR()
        }

        //所持状況
        var possession = findViewById<EditText>(R.id.editPossession)
        possession.isFocusable = false
        possession.setOnClickListener {
            dialogP()
        }

        //登録ボタン
        button.setOnClickListener {
            val dbHelper = BookDBHelper(applicationContext, "Book", null, 1)
            val database = dbHelper.writableDatabase

                try {
                    val dbHelper = BookDBHelper(applicationContext, "Book", null, 1);
                    val database = dbHelper.writableDatabase

                    val values = ContentValues()
                    values.put("id", 1)//("カラム名", 値)
                    values.put("title", "モゲます")
                    values.put("authorName", "やまだ")
                    values.put("publisher", "うどん")
                    values.put("issuedDate", 20200101)
                    values.put("recordDate", 20200101)
                    values.put("page", 100)
                    values.put("basicGenre", "")
                    values.put ("copyright", 0)
                    values.put("rating", 0)
                    values.put("favorite", 0)
                    values.put("describe","")
                    values.put("possession", 0)
                    values.put("price",0)
                    values.put("storageLocation","")
                    values.put("saveDate", 20201010)

                    database.insertOrThrow("Book", null, values)
                } catch (exception: Exception) {
                    Log.e("insertData", exception.toString())
                }
            }
        }



    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    //カレンダー
    private fun showDatePicker() {
        val body = findViewById<View?>(R.id.editIssuedDate) as EditText
        var dispBody = ""
        val datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { view, year, month, day ->
                dispBody = "${year}/${month + 1}/${day}"
                body.setText(dispBody, TextView.BufferType.NORMAL)
                setYear = year
                setMonth = month
                setDay = day
            },
            setYear, setMonth, setDay
        )
        datePickerDialog.show()
    }

    //ジャンルアラート
    private fun dialogG() {
        val body = findViewById<View?>(R.id.editGenre) as EditText
        AlertDialog.Builder(this) // FragmentではActivityを取得して生成
            .setTitle("作品ジャンル")
            .setItems(genreList) { dialog, which ->
                if (which == 0) {
                    body.setText("漫画", TextView.BufferType.NORMAL)
                } else if (which == 1) {
                    body.setText("雑誌", TextView.BufferType.NORMAL)
                } else if (which == 2) {
                    body.setText("小説", TextView.BufferType.NORMAL)
                }
            }
            .setPositiveButton("OK") { dialog, which ->
                // TODO:Yesが押された時の挙動
            }
            .show()
    }

    //対象年齢
    private fun dialogR() {
        val body = findViewById<View?>(R.id.editRating) as EditText
        AlertDialog.Builder(this) // FragmentではActivityを取得して生成
            .setTitle("対象年齢")
            .setItems(ratingList) { dialog, which ->
                if (which == 0) {
                    body.setText("全年齢対象", TextView.BufferType.NORMAL)
                } else if (which == 1) {
                    body.setText("春画", TextView.BufferType.NORMAL)
                } else if (which == 2) {
                    body.setText("R-18", TextView.BufferType.NORMAL)
                } else if (which == 3) {
                    body.setText("R-18G", TextView.BufferType.NORMAL)
                }
            }
            .setPositiveButton("OK") { dialog, which ->
                // TODO:Yesが押された時の挙動
            }
            .show()
    }

    //所持状況
    private fun dialogP() {
        val body = findViewById<View?>(R.id.editPossession) as EditText
        AlertDialog.Builder(this) // FragmentではActivityを取得して生成
            .setTitle("所持状況")
            .setItems(possessionList) { dialog, which ->
                if (which == 0) {
                    body.setText("未所持", TextView.BufferType.NORMAL)
                } else if (which == 1) {
                    body.setText("デジタル", TextView.BufferType.NORMAL)
                } else if (which == 2) {
                    body.setText("実物", TextView.BufferType.NORMAL)
                } else if (which == 3) {
                    body.setText("両方", TextView.BufferType.NORMAL)
                }
            }
            .setPositiveButton("OK") { dialog, which ->
                // TODO:Yesが押された時の挙動
            }
            .show()
    }


}



