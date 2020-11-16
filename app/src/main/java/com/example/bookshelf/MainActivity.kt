package com.example.bookshelf

import android.content.Context
import android.os.Bundle
import android.telephony.cdma.CdmaCellLocation
import android.view.Menu
import android.view.MenuItem
import android.view.View
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
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    companion object {
        //選択した本のナンバー
        var selectNo : Int = 0
        //表示するタイプ 0:リスト 1:グリッド
        var selectViewType : Int = 0

        //var no: Array<Int> = arrayOf(0)
        var bookTitle       : Array<String?> = arrayOfNulls(10000)
        var authorName      : Array<String?> = arrayOfNulls(10000)
        var publisher       : Array<String?> = arrayOfNulls(10000)
        var issuedDate      : Array<String?> = arrayOfNulls(10000)
        var recordDate      : Array<String?> = arrayOfNulls(10000)
        var page            : Array<Int?>    = arrayOfNulls(10000)
        var basicGenre      : Array<String?> = arrayOfNulls(10000)
        var copyright       : Array<Boolean> = Array(10000){false}
        var rating          : Array<Int?>    = arrayOfNulls(10000)
        var favorite        : Array<Boolean> = Array(10000){false}
        var userTag                          = Array(10000) {arrayOfNulls<String>(10)}
        var describe        : Array<String?> = arrayOfNulls(10000)
        var possession      : Array<Int?>    = arrayOfNulls(10000)
        var price           : Array<Int?>    = arrayOfNulls(10000)
        var storageLocation : Array<String?> = arrayOfNulls(10000)
        var getLocation     : Array<String?> = arrayOfNulls(10000)
        var saveDate        : Array<Boolean> = Array(10000){false}
        var maxNo:Int = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        save(0,"わんこ","集米社","もちこ","",
             "",0,"",false,0,false,
             "","","","","","",
             "","","","","",0,
             0, "","")
        userTag[1]
        //selectNo = 1
        load()

        maxNo = 2

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

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

    fun save(
        no              :Int,
        title           :String,
        authorName      :String,
        publisher       :String,
        issuedDate      :String,
        recordDate      :String,
        page            :Int,
        basicGenre      :String,
        copyright       :Boolean,
        rating          :Int,
        favorite        :Boolean,
        userTag1:String,userTag2:String,userTag3:String,userTag4:String,userTag5:String,
        userTag6:String,userTag7:String,userTag8:String,userTag9:String,userTag10:String,
        describe        :String,
        possession      :Int,
        price           :Int,
        storageLocation :String,
        getLocation     :String

    ){
        var data = getSharedPreferences("bookNo." + no.toString(), Context.MODE_PRIVATE)
        val editor = data.edit()
        editor.putString("TITLE", title)
        editor.putString("AUTHOR_NAME", authorName)
        editor.putString("PUBLISHER",publisher)
        //editor.putInt("userTagCnt", userTagCnt)
        /*for(i in 0..userTagCnt - 1){
            editor.putString("userTag$i", userTag[i])
        }*/
        editor.apply()
    }

    fun load() {
        //読み込む本の数
        for(loadNo in 0..100) {
            println("loadNo:"+loadNo)
            // 読み出し
            val data = getSharedPreferences("bookNo.$loadNo", Context.MODE_PRIVATE)
            bookTitle [loadNo] = data.getString ("TITLE"      , null)
            authorName[loadNo] = data.getString ("AUTHOR_NAME", null)
            publisher [loadNo] = data.getString ("PUBLISHER"  , null)
            issuedDate[loadNo] = data.getString ("ISSUED_DATE",null)
            recordDate[loadNo] = data.getString ("RECORD_DATE",null)
            page      [loadNo] = data.getInt    ("PAGE"       ,0)
            basicGenre[loadNo] = data.getString ("RECORD_DATE",null)
            copyright [loadNo] = data.getBoolean("COPYRIGHT"  ,false)
            rating    [loadNo] = data.getInt    ("PAGE"       ,0)
            favorite  [loadNo] = data.getBoolean("COPYRIGHT"  ,false)
            for(i in 0..9){
                userTag[loadNo][i]  = data.getString ("USER_TAG"        ,null)
            }
            describe       [loadNo] = data.getString ("DESCRIBE"        ,null)
            possession     [loadNo] = data.getInt    ("POSSESSION"            ,0)
            price          [loadNo] = data.getInt    ("PRICE"        ,0)
            storageLocation[loadNo] = data.getString ("STORAGE_LOCATION",null)
            getLocation    [loadNo] = data.getString ("GET_LOCATION"    ,null)
            saveDate       [loadNo] = data.getBoolean("SAVE_DATE"       ,false)
            println("TITLE:" + bookTitle[loadNo])
            println("AUTHOR_NAME:" + authorName[loadNo])
            println("PUBLISHER:" + publisher[loadNo])
        }

    }


}
