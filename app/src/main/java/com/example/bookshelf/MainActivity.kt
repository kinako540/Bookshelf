package com.example.bookshelf

import android.animation.StateListAnimator
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.os.Bundle
import android.telephony.cdma.CdmaCellLocation
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bookshelf.Database.BookDBHelper
import com.example.bookshelf.R.id.fab
import com.example.bookshelf.ui.gallery.GalleryFragment
import com.example.bookshelf.ui.home.HomeFragment
import com.example.bookshelf.ui.info.main.TextFragment
import com.example.bookshelf.ui.info.main.TopFragment
import com.example.bookshelf.ui.slideshow.SlideshowFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.common.base.Converter
import kotlinx.android.synthetic.main.activity_info.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_slideshow.*
import java.sql.Blob
import java.util.*
import kotlin.collections.ArrayList

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    companion object {
        //選択した本のナンバー
        var selectNo : Int = 0
        //表示するタイプ 0:リスト 1:グリッド
        var selectViewType : Int = 0
        //アプリ初回起動
        var load = false
       
        //TRUEの時startCameraを実行する
        var selectCameraIntent = false
        //TRUEの時startChooseImageを実行する
        var selectChooseImageIntent = false
        //var no: Array<Int> = arrayOf(0)
        var bookimage       : Array<Bitmap?> = arrayOfNulls(10000)
        var bookTitle       : Array<String?> = arrayOfNulls(10000)
        var authorName      : Array<String?> = arrayOfNulls(10000)
        var publisher       : Array<String?> = arrayOfNulls(10000)
        var issuedDate      : Array<String?> = arrayOfNulls(10000)
        var recordDate      : Array<String?> = arrayOfNulls(10000)
        var page            : Array<Int?>    = arrayOfNulls(10000)
        var basicGenre      : Array<String?> = arrayOfNulls(10000)
        var copyright       : Array<Int?>    = arrayOfNulls(10000)
        var rating          : Array<String?>    = arrayOfNulls(10000)
        var favorite        : Array<Int?>    = arrayOfNulls(10000)
        var userTag                          = Array(10000) {arrayOfNulls<String>(10)}
        var describe        : Array<String?> = arrayOfNulls(10000)
        var possession      : Array<Int?>    = arrayOfNulls(10000)
        var price           : Array<Int?>    = arrayOfNulls(10000)
        var storageLocation : Array<String?> = arrayOfNulls(10000)
        var getLocation     : Array<String?> = arrayOfNulls(10000)
        var saveDate        : Array<Int?>    = arrayOfNulls(10000)
        var maxNo:Int = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //deleteData("1")
        /*DeleteData("2")
        DeleteData("3")
        DeleteData("4")
        DeleteData("0")
        DeleteData("5")
        DeleteData("6")*/

        if(!load){
            selectData()
            load = true
        }



        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)


        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        //左側のナビゲーション
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        //setupWithNavController(bottom_navigation, navController)

        val bnv = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bnv.setOnNavigationItemSelectedListener { item ->
            when (item.itemId){
                R.id.nav_home -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.nav_host_fragment, HomeFragment())
                        .commit()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.nav_gallery   -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.nav_host_fragment, GalleryFragment())
                        .commit()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.nav_slideshow -> {}
                R.id.nav_setting   -> {}
            }
            return@setOnNavigationItemSelectedListener false
        }


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.list -> {
                selectViewType = 0
                finish()
                startActivity(getIntent())
            }
            R.id.grid -> {
                selectViewType = 1
                finish()
                startActivity(getIntent())
            }
            //else -> "Please select menu"
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
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
        //bottom_navigation.visibility = View.VISIBLE
    }
    private fun hideBottomNav() {
        //bottom_navigation.visibility = View.GONE
    }

    private fun selectData(){
        try {
            selectNo = 0
            val dbHelper = BookDBHelper(applicationContext, "Book", null, 1)
            val database = dbHelper.readableDatabase

            val sql = "select * from Book order by id"
            val cursor = database.rawQuery(sql, null)
            if (cursor.count > 0) {
                cursor.moveToFirst()
                while (!cursor.isAfterLast) {
                    //バイトからビットマップへ変換
                    val blob: ByteArray = cursor.getBlob(1)
                    val bitmap = BitmapFactory.decodeByteArray(blob, 0, blob.size)
                    println("bookTitle["+selectNo+"]:"+bookTitle[selectNo])
                    bookimage[selectNo]       = (bitmap)
                    bookTitle[selectNo]       = (cursor.getString(2))
                    authorName[selectNo]      = (cursor.getString(3))
                    publisher[selectNo]       = (cursor.getString(4))
                    issuedDate[selectNo]      = (cursor.getString(5))
                    recordDate[selectNo]      = (cursor.getString(6))
                    page[selectNo]            = (cursor.getInt(7))
                    basicGenre[selectNo]      = (cursor.getString(8))
                    copyright[selectNo]       = (cursor.getInt(9))
                    rating[selectNo]          = (cursor.getString(10))
                    favorite[selectNo]        = (cursor.getInt(11))
                    describe[selectNo]        = (cursor.getString(12))
                    possession[selectNo]      = (cursor.getInt(13))
                    price[selectNo]           = (cursor.getInt(14))
                    storageLocation[selectNo] = (cursor.getString(15))
                    saveDate[selectNo]        = (cursor.getInt(16))
                    selectNo += 1
                    if(bookTitle[selectNo] == null){
                        maxNo = selectNo
                    }
                    cursor.moveToNext()
                }
            }
        }catch(exception: Exception) {
            Log.e("selectData", exception.toString());
        }
    }

    fun deleteData(id : String){
        try {
            val dbHelper = BookDBHelper(applicationContext, "Book", null, 1);
            val database = dbHelper.writableDatabase

            val whereClauses = "id = ?"
            val whereArgs = arrayOf(id)
            database.delete("Book", whereClauses, whereArgs)
        }catch(exception: Exception) {
            Log.e("deleteData", exception.toString())
        }
    }
    fun reload(){
        MainActivity.load = false
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        load = false
    }

}
