package com.example.bookshelf.ui.create

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.bookshelf.BookDate
import com.example.bookshelf.Database.BookDBHelper
import com.example.bookshelf.MainActivity
import com.example.bookshelf.R
import com.example.bookshelf.ui.info.InfoActivity
import kotlinx.android.synthetic.main.activity_create.*
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.IOException

class CreateActivity : AppCompatActivity() {
    var setYear = 2020
    var setMonth = 1 - 1
    var setDay = 1

    // ジャンル
    val genreList = arrayOf("漫画", "雑誌", "小説","その他")
    val ratingList = arrayOf("全年齢対象", "過激な表現あり", "R-18", "R-18G")
    val possessionList = arrayOf("未所持", "デジタル", "実物", "両方")

    //版権
    private var copyright = false

    //画像
    var bookImage: Bitmap? = null

    //バーコード
    private var okHttpClient: OkHttpClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //バーコード
        if (BookDate.barcode[0] != null){
            // OkHttp通信クライアントをインスタンス化
            okHttpClient = OkHttpClient()
            // 通信するための情報
            // MainActivityで入力された文字列で検索する様修正
            var request: Request
            //ISBNコードがどっちに入ってるかチェック
            if (BookDate.barcode[0]!!.toLong() > 9780000000000){
                request = Request.Builder().url("https://www.googleapis.com/books/v1/volumes?q=isbn:9784163902302").build()
            }else{
                request = Request.Builder().url("https://www.googleapis.com/books/v1/volumes?q=isbn:${BookDate.barcode[1]}").build()
            }
            // データの取得後の命令を実装
            val callBack: Callback = object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    // 失敗した時の命令
                    // 通信に失敗した原因をログに出力
                    Log.e("failure API Response", e.localizedMessage)
                }

                @Throws(IOException::class)
                override fun onResponse(call: Call, response: Response) {
                    // 成功した時の命令
                    // Google Books APIから取得したデータをログに出力
                    // Jsonのパースが失敗してアプリの強制終了を回避する機能
                    try {
                        BookDate.barcode[0] = null
                        BookDate.barcode[1] = null
                        // JsonデータをJSONObjectに変換
                        val rootJson = JSONObject(response.body()!!.string())
                        // Jsonデータから蔵書リストデータ"items"を取得
                        val items = rootJson.getJSONArray("items")
                        Log.d(
                            "Success API Response", "APIから取得したデータの件数:" +
                                    items.length()
                        )
                        // Jsonのパースエラーが発生した時に備えてtry~catchする
                        try {
                            // 蔵書リストの件数分繰り返しタイトルをログ出力する
                            for (i in 0 until items.length()) {
                                // 蔵書リストから i番目のデータを取得
                                val item = items.getJSONObject(i)
                                // 蔵書のi番目データから蔵書情報のグループを取得
                                val volumeInfo = item.getJSONObject("volumeInfo")
                                //val imageLinks = volumeInfo.getJSONObject("imageLinks")
                                // タイトルデータをリストに追加
                                if (volumeInfo.has("title")) {
                                    val barcodeTitle = volumeInfo.getString("title")
                                    editTitle.setText(barcodeTitle, TextView.BufferType.NORMAL)
                                }
                                if (volumeInfo.has("authors")) {
                                    val barcodeAuthors = volumeInfo.getString("authors")
                                    val barcodeAuthorsReplace = barcodeAuthors.replace("[\"", "").replace(
                                        "\"]",
                                        ""
                                    )
                                    editAuthorName.setText(
                                        barcodeAuthorsReplace,
                                        TextView.BufferType.NORMAL
                                    )
                                }
                                if (volumeInfo.has("imageLinks")) {
                                    val imageLinks = volumeInfo.getJSONObject("imageLinks")
                                    val barcodeImage = imageLinks.getString("thumbnail")
                                    //実装前
                                    //val inputStream = URL(barcodeImage).openStream()
                                    //bookImage = BitmapFactory.decodeStream(inputStream)
                                    //val img = findViewById<ImageView>(R.id.imageButton)
                                    //img.setImageBitmap(bookImage)
                                }
                                if (volumeInfo.has("pageCount")) {
                                    val barcodePageCount = volumeInfo.getInt("pageCount")
                                    editPage.setText(
                                        barcodePageCount.toString(),
                                        TextView.BufferType.NORMAL
                                    )
                                }
                                if (volumeInfo.has("publishedDate")) {
                                    val barcodePublishedDate = volumeInfo.getString("publishedDate")
                                    val barcodePublishedDateReplace = barcodePublishedDate.replace(
                                        "-",
                                        "/"
                                    )
                                    editIssuedDate.setText(
                                        barcodePublishedDateReplace,
                                        TextView.BufferType.NORMAL
                                    )
                                }
                                /*if (volumeInfo.has("printType")) {
                                    val barcodePrintType = volumeInfo.getString("printType")
                                    editGenre.setText(barcodePrintType, TextView.BufferType.NORMAL)
                                }*/
                                //if (volumeInfo.has("categories")) {
                                //barcodeCategoriesList.add(volumeInfo.getString("categories"))
                                //}
                            }
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    } catch (e: JSONException) {
                        // Jsonパースの時にエラーが発生したらログに出力する
                        e.printStackTrace()
                    }
                }
            }
            // 非同期処理でREST API通信を実行
            okHttpClient!!.newCall(request).enqueue(callBack)
        }

        //イメージボタン
        var img = findViewById<ImageButton>(R.id.imageButton)
        img.isFocusable = false
        img.setOnClickListener {
            selectPhoto()
        }

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
                    //("カラム名", 値)
                    var imageBit = bookImage
                    val size = imageBit!!.width * imageBit!!.height
                    println("サイズ："+size)
                    val matrix = Matrix()
                    if(size >= 10000000){
                        matrix.postScale(0.1f, 0.1f) // 0.1倍調整
                    }
                    else if(size >= 10000000){
                        matrix.postScale(0.5f, 0.5f) // 0.5倍調整
                    }
                    else{
                        matrix.postScale(1f, 1f) // 1倍調整
                    }
                    val scaledBitmap = Bitmap.createBitmap(imageBit!!, 0, 0, imageBit.width, imageBit.height, matrix, true)
                    val byteArrayOutputStream = ByteArrayOutputStream()
                    scaledBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
                    val image = byteArrayOutputStream.toByteArray()
                    values.put("image", image)
                    values.put("title", editTitle.text.toString())
                    values.put("authorName", editAuthorName.text.toString())
                    values.put("publisher", editPublisher.text.toString())
                    values.put("issuedDate", editIssuedDate.text.toString())
                    values.put("recordDate", "")
                    values.put("page", editPage.text.toString())
                    values.put("basicGenre", editGenre.text.toString())
                    values.put("copyright", editCopyright.text.toString())
                    values.put("rating", editRating.text.toString())
                    values.put("favorite", "")
                    values.put("describe", editDescribe.text.toString())
                    values.put("possession", editPossession.text.toString())
                    values.put("price", "")
                    values.put("storageLocation", "")
                    values.put("saveDate", "")
                    values.put("linkName", editLinkName.text.toString())
                    values.put("linkURL", editLinkURL.text.toString())
                    database.insertOrThrow("Book", null, values)

                    MainActivity.load = false
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
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
                } else if (which == 3) {
                    body.setText("その他", TextView.BufferType.NORMAL)
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
                    body.setText("過激な表現あり", TextView.BufferType.NORMAL)
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

    private fun selectPhoto() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "image/*"
        }
        startActivityForResult(intent, READ_REQUEST_CODE)
    }

    companion object {
        private const val READ_REQUEST_CODE: Int = 42
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, resultData: Intent?) {
        super.onActivityResult(requestCode, resultCode, resultData)
        if (resultCode != RESULT_OK) {
            return
        }
        when (requestCode) {
            READ_REQUEST_CODE -> {
                try {
                    resultData?.data?.also { uri ->
                        val inputStream = contentResolver?.openInputStream(uri)
                        bookImage = BitmapFactory.decodeStream(inputStream)
                        val img = findViewById<ImageView>(R.id.imageButton)
                        img.setImageBitmap(bookImage)
                    }
                } catch (e: Exception) {
                    Toast.makeText(this, "エラーが発生しました", Toast.LENGTH_LONG).show()
                }
            }
        }
    }


}



