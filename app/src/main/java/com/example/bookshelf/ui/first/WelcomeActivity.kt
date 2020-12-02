package com.example.bookshelf.ui.first

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.example.bookshelf.MainActivity
import com.example.bookshelf.R
import com.example.bookshelf.ui.first.ui.main.PlaceholderFragment
import com.example.bookshelf.ui.first.ui.main.SectionsPagerAdapter

class WelcomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val first: SharedPreferences = getSharedPreferences("First", Context.MODE_PRIVATE)

        //起動したことあるかチェック
        val check = first.getInt("FirstInt", 0)

        //起動したことある場合メインへ移動
        if (check == 1) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        } else {
            setContentView(R.layout.activity_welcome)

            //フラグメントを呼び出し
            val welcomeFragment = PlaceholderFragment()
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.add(R.id.view_pager, welcomeFragment)
            fragmentTransaction.commit()

            val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
            val viewPager: ViewPager = findViewById(R.id.view_pager)
            viewPager.adapter = sectionsPagerAdapter
            val tabs: TabLayout = findViewById(R.id.tabs)
            tabs.setupWithViewPager(viewPager)
        }
    }
    fun onButtonTapped(view : View?){
        val intent = Intent(this, MainActivity::class.java)

        //ボタンを押したときに変数変更
        val first =getSharedPreferences("First", Context.MODE_PRIVATE)
        val editor = first.edit()
        editor.putInt("FirstInt",1)

        //確定
        editor.apply()

        //画面遷移
        startActivity(intent)
    }
}