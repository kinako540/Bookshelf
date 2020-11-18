package com.example.bookshelf.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.bookshelf.MainActivity
import com.example.bookshelf.R
import com.example.bookshelf.ui.gallery.GalleryFragment
import com.example.bookshelf.ui.home2.Home2Fragment
import com.example.bookshelf.ui.slideshow.SlideshowFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_main.*

class HomeActivity : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->

        when (item.itemId) {
            R.id.nav_home -> {
                println("押されたよ")
                supportFragmentManager.beginTransaction()
                    .replace(R.id.frameLayout, HomeFragment())
                    .commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_gallery -> {
                println("押されたよ")
                supportFragmentManager.beginTransaction()
                    .replace(R.id.frameLayout, GalleryFragment())
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
        setContentView(R.layout.activity_home)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        bottom_navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        //setupNav()
    }

    //下部のナビゲーションドロワーの表示、非表示
    private fun setupNav() {
        val navController = findNavController(R.id.nav_host_fragment)
        findViewById<BottomNavigationView>(R.id.bottom_navigation)
            .setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                //ナビゲーションドロワーを表示
                R.id.nav_home -> showBottomNav()
                //ナビゲーションドロワーを非表示
                else -> hideBottomNav()
            }
        }
    }
    private fun showBottomNav() {
        bottom_navigation.visibility = View.VISIBLE
    }
    private fun hideBottomNav() {
        bottom_navigation.visibility = View.GONE
    }

    override fun onSupportNavigateUp(): Boolean {
        val intent = Intent(application, MainActivity::class.java)
        startActivity(intent)
        finish()
        return super.onSupportNavigateUp()
    }
}