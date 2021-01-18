package com.example.bookshelf.ui.info

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.example.bookshelf.MainActivity
import com.example.bookshelf.R
import com.example.bookshelf.ui.gallery.GalleryFragment
import com.example.bookshelf.ui.home.SlideshowFragment
import com.example.bookshelf.ui.info.main.TextFragment
import com.example.bookshelf.ui.info.main.TopFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_info.*

class InfoActivity : AppCompatActivity() {
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->

        when (item.itemId) {
            R.id.nav_home -> {
                println("押されたよ")
                supportFragmentManager.beginTransaction()
                    .replace(R.id.frameLayout, TopFragment())
                    .commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_gallery -> {
                println("押されたよ")
                supportFragmentManager.beginTransaction()
                    .replace(R.id.frameLayout, TextFragment())
                    .commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_slideshow -> {
                println("押されたよ")
                supportFragmentManager.beginTransaction()
                    .replace(R.id.frameLayout, SlideshowFragment())
                    .commit()
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)
        val ma = MainActivity
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        bottom_navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        val fragment = TopFragment()
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.frameLayout, fragment)
        transaction.commit()
    }
}