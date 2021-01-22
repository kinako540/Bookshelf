package com.example.bookshelf

import android.animation.StateListAnimator
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Bundle
import android.telephony.cdma.CdmaCellLocation
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
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
import com.example.bookshelf.R.id.search
import com.example.bookshelf.ui.gallery.GalleryFragment
import com.example.bookshelf.ui.home.HomeFragment
import com.example.bookshelf.ui.info.main.TextFragment
import com.example.bookshelf.ui.info.main.TopFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.common.base.Converter
import kotlinx.android.synthetic.main.activity_info.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_slideshow.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
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
        //削除時
        var del = false
       
        //TRUEの時startCameraを実行する
        var selectCameraIntent = false
        //TRUEの時startChooseImageを実行する
        var selectChooseImageIntent = false

        //------------------------------------------------------------------------------------------
        // オリジナル
        //------------------------------------------------------------------------------------------
        var bookID          : Array<Int?>    = arrayOfNulls(10000)
        var bookimage       : Array<Bitmap?> = arrayOfNulls(10000)
        var bookTitle       : Array<String?> = arrayOfNulls(10000)
        var authorName      : Array<String?> = arrayOfNulls(10000)
        var publisher       : Array<String?> = arrayOfNulls(10000)
        var issuedDate      : Array<String?> = arrayOfNulls(10000)
        var recordDate      : Array<String?> = arrayOfNulls(10000)
        var page            : Array<Int?>    = arrayOfNulls(10000)
        var basicGenre      : Array<String?> = arrayOfNulls(10000)
        var copyright       : Array<Int?>    = arrayOfNulls(10000)
        var rating          : Array<String?> = arrayOfNulls(10000)
        var favorite        : Array<Int?>    = arrayOfNulls(10000)
        var userTag                          = Array(10000) {arrayOfNulls<String>(10)}
        var describe        : Array<String?> = arrayOfNulls(10000)
        var possession      : Array<String?>    = arrayOfNulls(10000)
        var price           : Array<Int?>    = arrayOfNulls(10000)
        var storageLocation : Array<String?> = arrayOfNulls(10000)
        var getLocation     : Array<String?> = arrayOfNulls(10000)
        var saveDate        : Array<Int?>    = arrayOfNulls(10000)
        var linkName        : Array<String?> = arrayOfNulls(10000)
        var linkURL         : Array<String?> = arrayOfNulls(10000)

        //------------------------------------------------------------------------------------------
        // テンプ
        //------------------------------------------------------------------------------------------
        var T_bookID          : Array<Int?>    = arrayOfNulls(10000)
        var T_bookimage       : Array<Bitmap?> = arrayOfNulls(10000)
        var T_bookTitle       : Array<String?> = arrayOfNulls(10000)
        var T_authorName      : Array<String?> = arrayOfNulls(10000)
        var T_publisher       : Array<String?> = arrayOfNulls(10000)
        var T_issuedDate      : Array<String?> = arrayOfNulls(10000)
        var T_recordDate      : Array<String?> = arrayOfNulls(10000)
        var T_page            : Array<Int?>    = arrayOfNulls(10000)
        var T_basicGenre      : Array<String?> = arrayOfNulls(10000)
        var T_copyright       : Array<Int?>    = arrayOfNulls(10000)
        var T_rating          : Array<String?> = arrayOfNulls(10000)
        var T_favorite        : Array<Int?>    = arrayOfNulls(10000)
        var T_userTag                          = Array(10000) {arrayOfNulls<String>(10)}
        var T_describe        : Array<String?> = arrayOfNulls(10000)
        var T_possession      : Array<String?>    = arrayOfNulls(10000)
        var T_price           : Array<Int?>    = arrayOfNulls(10000)
        var T_storageLocation : Array<String?> = arrayOfNulls(10000)
        var T_getLocation     : Array<String?> = arrayOfNulls(10000)
        var T_saveDate        : Array<Int?>    = arrayOfNulls(10000)
        var T_linkName        : Array<String?> = arrayOfNulls(10000)
        var T_linkURL         : Array<String?> = arrayOfNulls(10000)

        var maxNo:Int = 0
        var defaultMaxNo:Int = 0

        //検索条件
        var S_genre :Int = 99
        var S_rating :Int = 99
        var S_position :Int = 99
        var S_tag :Int = 99
        var maxCnt1:Int = 0
        var maxCnt2:Int = 0
        var maxCnt3:Int = 0
        var maxCnt4:Int = 0
        var masterMaxCnt:Int = 0
        var masterTempNo: Array<Int?> = arrayOfNulls(10000)
        var tempNo1: Array<Int?> = arrayOfNulls(10000)
        var tempNo2: Array<Int?> = arrayOfNulls(10000)
        var tempNo3: Array<Int?> = arrayOfNulls(10000)
        var tempNo4: Array<Int?> = arrayOfNulls(10000)

        //検索バー
        var searchBar = false
        var buttonS = false
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
                }
                R.id.nav_gallery   -> {
                    selectContents()
                }
                R.id.nav_slideshow -> {
                    if(buttonS == false) {
                        buttonS = true
                        searchBar = true
                        search.visibility = View.VISIBLE
                        //検索バー
                        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                            override fun onQueryTextChange(newText: String): Boolean {
                                return false
                            }

                            override fun onQueryTextSubmit(query: String): Boolean {
                                searchDate(query)
                                finish()
                                startActivity(getIntent())
                                buttonS = false
                                searchBar = false
                                println("検索開始")
                                return false
                            }
                        })
                    }
                    else if(buttonS == true) {
                        buttonS = false
                        searchBar = false
                        search.visibility = View.GONE
                    }
                }
                R.id.nav_clear   -> {
                    ClearDialog()
                }
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
                    bookID[selectNo]          = (cursor.getInt(0))
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
                    possession[selectNo]      = (cursor.getString(13))
                    price[selectNo]           = (cursor.getInt(14))
                    storageLocation[selectNo] = (cursor.getString(15))
                    saveDate[selectNo]        = (cursor.getInt(16))
                    linkName[selectNo]        = (cursor.getString(17))
                    linkURL[selectNo]         = (cursor.getString(18))
                    selectNo += 1
                    if(bookTitle[selectNo] == null){
                        maxNo = selectNo
                    }
                    cursor.moveToNext()
                }
            }
            if(del){
                del = false
                maxNo =  maxNo -1
            }
            defaultMaxNo = maxNo
            println("最大値は"+maxNo)
        }catch(exception: Exception) {
            Log.e("selectData", exception.toString())
        }
    }
    fun searchDate(name:String){
        try {
            val search = "'"+ name +"%'"
            println("検索:+$search")
            clearDate()
            selectNo = 0
            val dbHelper = BookDBHelper(applicationContext, "Book", null, 1)
            val database = dbHelper.readableDatabase

            val sql = "select * from Book where title like $search OR authorName like $search OR publisher like $search"
            val cursor = database.rawQuery(sql, null)
            if (cursor.count > 0) {
                cursor.moveToFirst()
                while (!cursor.isAfterLast) {
                    //バイトからビットマップへ変換
                    val blob: ByteArray = cursor.getBlob(1)
                    val bitmap = BitmapFactory.decodeByteArray(blob, 0, blob.size)
                    println("bookTitle["+selectNo+"]:"+bookTitle[selectNo])
                    bookID[selectNo]          = (cursor.getInt(0))
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
                    possession[selectNo]      = (cursor.getString(13))
                    price[selectNo]           = (cursor.getInt(14))
                    storageLocation[selectNo] = (cursor.getString(15))
                    saveDate[selectNo]        = (cursor.getInt(16))
                    linkName[selectNo]        = (cursor.getString(17))
                    linkURL[selectNo]         = (cursor.getString(18))
                    selectNo += 1
                    if(bookTitle[selectNo] == null){
                        maxNo = selectNo
                    }
                    cursor.moveToNext()
                }
            }
            if(del){
                del = false
                maxNo =  maxNo -1
            }
            defaultMaxNo = maxNo
            println("最大値は"+maxNo)
            if(bookID[0] == null){
                maxNo = 0
            }
        }catch(exception: Exception) {
            Log.e("selectData", exception.toString())
        }
        finish()
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(getIntent())
    }

    fun deleteData(id : String){
        try {
            del = true
            val dbHelper = BookDBHelper(applicationContext, "Book", null, 1);
            val database = dbHelper.writableDatabase

            val whereClauses = "id = ?"
            val whereArgs = arrayOf(id)
            database.delete("Book", whereClauses, whereArgs)
            reload()
        }catch(exception: Exception) {
            Log.e("deleteData", exception.toString())
        }
    }
    fun reload(){
        GlobalScope.launch {
            selectData()
        }
        Thread.sleep(100)
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
    //----------------------------------------------------------------------------------------------
    //検索条件
    //----------------------------------------------------------------------------------------------
    private fun selectContents(){
        val strList = arrayOf("作品ジャンル","対象年齢","所持状況","タグ")

        AlertDialog.Builder(this) // FragmentではActivityを取得して生成
            .setTitle("検索条件")
            .setItems(strList) { dialog, which ->
                println("押された→"+strList[which])
                when (which) {
                    0 -> { selectGenre() }
                    1 -> { selectRating() }
                    2 -> { selectPosition() }
                    3 -> {}
                    else -> {}
                }
            }
            .setNegativeButton("キャンセル") { dialog, which ->
                // TODO:Noが押された時の挙動
            }
            .setNeutralButton("クリア") { dialog, which ->
                ClearDialog()
            }
            .show()
    }
    //作品ジャンル
    private fun selectGenre(){
        if(S_genre != 99){
            allClear()
        }
        S_genre = 0
        val strList = arrayOf("漫画","雑誌","小説","その他")
        AlertDialog.Builder(this) // FragmentではActivityを取得して生成
            .setTitle("ラジオボタン選択ダイアログ")
            .setSingleChoiceItems(strList, 0) { dialog, which ->
                when (which) {
                    0 -> {S_genre = 0}
                    1 -> {S_genre = 1}
                    2 -> {S_genre = 2}
                    3 -> {S_genre = 3}
                    else -> {S_genre = 0}
                }
            }
            .setPositiveButton("OK") { dialog, which ->
                createDate()
            }
            .setNegativeButton("キャンセル") { dialog, which ->
                // TODO:Noが押された時の挙動
            }
            .setNeutralButton("クリア") { dialog, which ->
                // TODO:その他が押された時の挙動
            }
            .show()
    }
    //対象年齢
    private fun selectRating(){
        if(S_rating != 99){
            allClear()
        }
        S_rating = 0
        val strList = arrayOf("全年齢対象","過激な表現あり","R-18","R-18G")
        AlertDialog.Builder(this) // FragmentではActivityを取得して生成
            .setTitle("対象年齢")
            .setSingleChoiceItems(strList, 0) { dialog, which ->
                when (which) {
                    0 -> {S_rating = 0}
                    1 -> {S_rating = 1}
                    2 -> {S_rating = 2}
                    3 -> {S_rating = 3}
                    else -> {S_rating = 0}
                }
            }
            .setPositiveButton("OK") { dialog, which ->
                createDate()
            }
            .setNegativeButton("キャンセル") { dialog, which ->
                // TODO:Noが押された時の挙動
            }
            .setNeutralButton("クリア") { dialog, which ->
                // TODO:その他が押された時の挙動
            }
            .show()
    }
    //所持状況
    private fun selectPosition(){
        if(S_position != 99){
            allClear()
        }
        S_position = 0
        val strList = arrayOf("未所持","デジタル","実物","両方")
        AlertDialog.Builder(this) // FragmentではActivityを取得して生成
            .setTitle("所持状況")
            .setSingleChoiceItems(strList, 0) { dialog, which ->
                when (which) {
                    0 -> { S_position = 0}
                    1 -> { S_position = 1}
                    2 -> { S_position = 2}
                    3 -> { S_position = 3}
                    else -> { S_position= 0}
                }
            }
            .setPositiveButton("OK") { dialog, which ->
                createDate()
            }
            .setNegativeButton("キャンセル") { dialog, which ->
                // TODO:Noが押された時の挙動
            }
            .setNeutralButton("クリア") { dialog, which ->
                // TODO:その他が押された時の挙動
            }
            .show()
    }
    //条件検索を全てのクリアする
    private fun ClearDialog(){
        AlertDialog.Builder(this) // FragmentではActivityを取得して生成
            .setTitle("クリア")
            .setMessage("全ての条件式をクリアしますか？")
            .setPositiveButton("OK") { dialog, which ->
                allClear()
                finish()
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(getIntent())
            }
            .setNegativeButton("キャンセル") { dialog, which ->
                // TODO:Noが押された時の挙動
            }
            .show()
    }
    private fun allClear(){
        //条件検索を削除
        tempNo1 = arrayOfNulls(10000)
        tempNo2 = arrayOfNulls(10000)
        tempNo3 = arrayOfNulls(10000)
        tempNo4 = arrayOfNulls(10000)
        S_genre = 99
        S_rating = 99
        S_position = 99
        S_tag = 99
        maxCnt1 = 0
        maxCnt2 = 0
        maxCnt3 = 0
        maxCnt4 = 0
        masterMaxCnt = 0
        masterTempNo = arrayOfNulls(10000)
        clearDate()
        selectData()
    }
    private fun createDate(){
        T_bookID = bookID
        T_bookimage = bookimage
        T_bookTitle = bookTitle
        T_authorName = authorName
        T_publisher = publisher
        T_issuedDate = issuedDate
        T_recordDate = recordDate
        T_page = page
        T_basicGenre = basicGenre
        T_copyright = copyright
        T_rating = rating
        T_favorite = favorite
        T_describe = describe
        T_possession = possession
        T_price = price
        T_storageLocation = storageLocation
        T_getLocation = getLocation
        T_saveDate = saveDate
        T_linkName = linkName
        T_linkURL  = linkURL

        //漫画
        println("S_genre = "+S_genre)
        if(S_genre == 0){
            var cnt:Int = 0
            for(i in 0..maxNo){
                if(T_basicGenre[i] == "漫画"){
                    tempNo1[cnt] = i
                    cnt++
                    maxCnt1 = cnt
                }
            }
        }
        if(S_genre == 1){
            var cnt:Int = 0
            for(i in 0..maxNo){
                if(T_basicGenre[i] == "雑誌"){
                    tempNo1[cnt] = i
                    cnt++
                    maxCnt1 = cnt
                }
            }
        }
        if(S_genre == 2){
            var cnt:Int = 0
            for(i in 0..maxNo){
                if(T_basicGenre[i] == "小説"){
                    tempNo1[cnt] = i
                    cnt++
                    maxCnt1 = cnt
                }
            }
        }
        if(S_genre == 3){
            var cnt:Int = 0
            for(i in 0..maxNo){
                if(T_basicGenre[i] == "その他"){
                    tempNo1[cnt] = i
                    cnt++
                    maxCnt1 = cnt
                }
            }
        }
        //------------------------------------------------------------------------------------------
        //対象年齢
        println("S_rating = "+S_rating)
        if(S_rating == 0){
            var cnt:Int = 0
            for(i in 0..maxNo){
                println("T_rating[i]="+T_rating[i])
                if(T_rating[i] == "全年齢対象"){
                    tempNo2[cnt] = i
                    cnt++
                    maxCnt2 = cnt
                }
            }
        }
        if(S_rating == 1){
            var cnt:Int = 0
            for(i in 0..maxNo){
                if(T_rating[i] == "過激な表現あり"){
                    tempNo2[cnt] = i
                    cnt++
                    maxCnt2 = cnt
                }
            }
        }
        if(S_rating == 2){
            var cnt:Int = 0
            for(i in 0..maxNo){
                if(T_rating[i] == "R-18"){
                    tempNo2[cnt] = i
                    cnt++
                    maxCnt2 = cnt
                }
            }
        }
        if(S_rating == 3){
            var cnt:Int = 0
            for(i in 0..maxNo){
                if(T_rating[i] == "R-18G"){
                    tempNo2[cnt] = i
                    cnt++
                    maxCnt2 = cnt
                }
            }
        }
        //------------------------------------------------------------------------------------------
        //対象年齢
        println("S_position = "+ S_position)
        if(S_position == 0){
            var cnt:Int = 0
            for(i in 0..maxNo){
                println("T_position[i]="+ T_possession[i])
                if(T_possession[i] == "未所持"){
                    tempNo3[cnt] = i
                    cnt++
                    maxCnt3 = cnt
                }
            }
        }
        if(S_position == 1){
            var cnt:Int = 0
            for(i in 0..maxNo){
                println("T_position[i]="+ T_possession[i])
                if(T_possession[i] == "デジタル"){
                    tempNo3[cnt] = i
                    cnt++
                    maxCnt3 = cnt
                }
            }
        }
        if(S_position == 2){
            var cnt:Int = 0
            for(i in 0..maxNo){
                println("T_position[i]="+ T_possession[i])
                if(T_possession[i] == "実物"){
                    tempNo3[cnt] = i
                    cnt++
                    maxCnt3 = cnt
                }
            }
        }
        if(S_position == 3){
            var cnt:Int = 0
            for(i in 0..maxNo){
                println("T_position[i]="+ T_possession[i])
                if(T_possession[i] == "両方"){
                    tempNo3[cnt] = i
                    cnt++
                    maxCnt3 = cnt
                }
            }
        }
        //統合
        var cnt:Int = 0
        //ジャンルと対象年齢と所持状況
        if(S_genre != 99 && S_rating != 99 && S_position != 99) {
            for (i1 in 0..maxCnt1 - 1) {
                for (i2 in 0..maxCnt2 - 1) {
                    for (i3 in 0..maxCnt3 - 1) {
                        if (tempNo1[i1] == tempNo2[i2] && tempNo1[i1] == tempNo3[i3]) {
                            println("一致：tempNo1[" + i1 + "] = " + tempNo1[i1] + " == tempNo2[" + i2 + "] = " + tempNo2[i2])
                            masterTempNo[cnt] = tempNo3[i3]
                            cnt++
                            masterMaxCnt = cnt
                        }
                    }
                }
            }
        }
        //ジャンルと対象年齢
        else if(S_genre != 99 && S_rating != 99){
            for(i1 in 0..maxCnt1-1) {
                for (i2 in 0..maxCnt2-1) {
                    if(tempNo1[i1] == tempNo2[i2]){
                        println("一致：tempNo1["+i1+"] = "+ tempNo1[i1] + " == tempNo2["+i2+"] = "+ tempNo2[i2])
                        masterTempNo[cnt] = tempNo2[i2]
                        cnt++
                        masterMaxCnt = cnt
                    }
                }
            }
        }
        //ジャンルと所持状況で条件付け
        else if(S_genre != 99 && S_position != 99){
            for(i1 in 0..maxCnt1-1) {
                for (i2 in 0..maxCnt3-1) {
                    if (tempNo1[i1] == tempNo3[i2]) {
                        println("一致：tempNo1[" + i1 + "] = " + tempNo1[i1] + " == tempNo3[" + i2 + "] = " + tempNo3[i2])
                        masterTempNo[cnt] = tempNo3[i2]
                        cnt++
                        masterMaxCnt = cnt
                    }
                }
            }
        }
        else if(S_genre != 99 && S_rating == 99 && S_position == 99){
            masterTempNo = tempNo1
            masterMaxCnt = maxCnt1
        }
        else if(S_genre == 99 && S_rating != 99 && S_position == 99){
            masterTempNo = tempNo2
            masterMaxCnt = maxCnt2
        }
        else if(S_genre == 99 && S_rating == 99 && S_position != 99){
            masterTempNo = tempNo3
            masterMaxCnt = maxCnt3
        }
        clearDate()
        println("masterMaxCnt = "+masterMaxCnt)
        maxNo = masterMaxCnt

        for(i in 0..masterMaxCnt-1){
            bookID[i] = T_bookID[masterTempNo[i]!!]
            bookimage[i]= T_bookimage[masterTempNo[i]!!]
            bookTitle[i] = T_bookTitle[masterTempNo[i]!!]
            authorName[i] = T_authorName[masterTempNo[i]!!]
            publisher[i] = T_publisher[masterTempNo[i]!!]
            issuedDate[i] = T_issuedDate[masterTempNo[i]!!]
            recordDate[i] = T_recordDate[masterTempNo[i]!!]
            page[i] = T_page[masterTempNo[i]!!]
            basicGenre[i] = T_basicGenre[masterTempNo[i]!!]
            copyright[i] = T_copyright[masterTempNo[i]!!]
            rating[i] = T_rating[masterTempNo[i]!!]
            favorite[i] = T_favorite[masterTempNo[i]!!]
            describe[i] = T_describe[masterTempNo[i]!!]
            possession[i] = T_possession[masterTempNo[i]!!]
            price[i] = T_page[masterTempNo[i]!!]
            storageLocation[i] = T_storageLocation[masterTempNo[i]!!]
            getLocation[i] = T_getLocation[masterTempNo[i]!!]
            saveDate[i] = T_saveDate[masterTempNo[i]!!]
            linkName[i] = T_linkName[masterTempNo[i]!!]
            linkURL[i] = T_linkURL[masterTempNo[i]!!]
        }
        tempNo1 = arrayOfNulls(10000)
        tempNo2 = arrayOfNulls(10000)
        tempNo3 = arrayOfNulls(10000)
        tempNo4 = arrayOfNulls(10000)
        masterTempNo = arrayOfNulls(10000)
        masterMaxCnt = 0
        finish()
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(getIntent())
    }
    //変数初期化
    private fun clearDate() {
        bookID = arrayOfNulls(10000)
        bookimage = arrayOfNulls(10000)
        bookTitle = arrayOfNulls(10000)
        authorName = arrayOfNulls(10000)
        publisher = arrayOfNulls(10000)
        issuedDate = arrayOfNulls(10000)
        recordDate = arrayOfNulls(10000)
        page = arrayOfNulls(10000)
        basicGenre = arrayOfNulls(10000)
        copyright = arrayOfNulls(10000)
        rating = arrayOfNulls(10000)
        favorite = arrayOfNulls(10000)
        describe = arrayOfNulls(10000)
        possession = arrayOfNulls(10000)
        price = arrayOfNulls(10000)
        storageLocation = arrayOfNulls(10000)
        getLocation = arrayOfNulls(10000)
        saveDate = arrayOfNulls(10000)
        linkName = arrayOfNulls(10000)
        linkURL = arrayOfNulls(10000)
    }
    //終了dialog
    fun shutdownLog(){
        android.app.AlertDialog.Builder(this) // FragmentではActivityを取得して生成
            .setTitle("終了")
            .setNegativeButton("キャンセル", null)
            .setMessage("アプリを終了しますか？")
            .setPositiveButton("OK") { dialog, which ->
            }
            .show()
    }
    //起動
    public fun startBrowser(url:String){
        val uri = Uri.parse("url")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }


}
