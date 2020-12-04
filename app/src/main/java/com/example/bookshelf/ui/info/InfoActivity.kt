package com.example.bookshelf.ui.info

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.example.bookshelf.MainActivity
import com.example.bookshelf.R
import com.example.bookshelf.ui.gallery.GalleryFragment
import com.example.bookshelf.ui.info.main.TextFragment
import com.example.bookshelf.ui.info.main.TopFragment
import com.example.bookshelf.ui.slideshow.SlideshowFragment
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

        /*/↓↓↓↓データベース出来たら消す↓↓↓↓
        /ma.selectNo = 1
        ma.bookTitle  [ma.selectNo] = "モゲます 総集編"
        ma.publisher  [ma.selectNo] = "もげもげ"
        ma.authorName [ma.selectNo] = "まげ"
        ma.issuedDate [ma.selectNo] = "2020/10/04"
        ma.recordDate [ma.selectNo] = "2020/11/04"
        ma.basicGenre [ma.selectNo] = "アイドルマスター"
        ma.page       [ma.selectNo] = 20
        ma.rating     [ma.selectNo] = 1*/
        //↑↑↑↑データベース出来たら消す↑↑↑↑

        val fragment = TopFragment()
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.frameLayout, fragment)
        transaction.commit()
    }
}