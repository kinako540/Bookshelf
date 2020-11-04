package com.example.bookshelf.ui.info

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.bookshelf.MainActivity
import com.example.bookshelf.R
import kotlinx.android.synthetic.main.activity_create.*
import kotlinx.android.synthetic.main.activity_info.*
import kotlinx.android.synthetic.main.activity_info.textAuthorName
import kotlinx.android.synthetic.main.activity_info.textIssuedDate
import kotlinx.android.synthetic.main.activity_info.textPublisher
import kotlinx.android.synthetic.main.activity_info.textTitle
import kotlinx.coroutines.selects.select
import kotlinx.android.synthetic.main.activity_create.textPage as textPage1
import kotlinx.android.synthetic.main.activity_create.textRating as textRating1

class InfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)
        val ma = MainActivity

        //↓↓↓↓データベース出来たら消す↓↓↓↓
        ma.selectNo = 1
        ma.bookTitle  [ma.selectNo] = "モゲます 総集編"
        ma.publisher  [ma.selectNo] = "もげもげ"
        ma.authorName [ma.selectNo] = "まげ"
        ma.issuedDate [ma.selectNo] = "2020/10/04"
        ma.recordDate [ma.selectNo] = "2020/11/04"
        ma.basicGenre [ma.selectNo] = "アイドルマスター"
        ma.page       [ma.selectNo] = 20
        ma.rating     [ma.selectNo] = 1
        //↑↑↑↑データベース出来たら消す↑↑↑↑
        //対象年齢
        if(ma.rating[ma.selectNo] == 1) {
            textSafeIcon.setVisibility(View.VISIBLE)
            textSpicyIcon.setVisibility(View.GONE)
            textR_18Icon.setVisibility(View.GONE)
            textGrotesqueIcon.setVisibility(View.GONE)
            textNonIcon.setVisibility(View.GONE)
        }
        else if(ma.rating[ma.selectNo] == 2){
            textSafeIcon.setVisibility(View.GONE)
            textSpicyIcon.setVisibility(View.VISIBLE)
            textR_18Icon.setVisibility(View.GONE)
            textGrotesqueIcon.setVisibility(View.GONE)
            textNonIcon.setVisibility(View.GONE)
        }
        else if(ma.rating[ma.selectNo] == 3){
            textSafeIcon.setVisibility(View.GONE)
            textSpicyIcon.setVisibility(View.GONE)
            textR_18Icon.setVisibility(View.VISIBLE)
            textGrotesqueIcon.setVisibility(View.GONE)
            textNonIcon.setVisibility(View.GONE)
        }
        else if(ma.rating[ma.selectNo] == 4){
            textSafeIcon.setVisibility(View.GONE)
            textSpicyIcon.setVisibility(View.GONE)
            textR_18Icon.setVisibility(View.GONE)
            textGrotesqueIcon.setVisibility(View.VISIBLE)
            textNonIcon.setVisibility(View.GONE)
        }
        else{
            textSafeIcon.setVisibility(View.GONE)
            textSpicyIcon.setVisibility(View.GONE)
            textR_18Icon.setVisibility(View.GONE)
            textGrotesqueIcon.setVisibility(View.GONE)
            textNonIcon.setVisibility(View.VISIBLE)
        }

        bookName.text        = ma.bookTitle[ma.selectNo]
        textTitle.text       = "タイトル：" + ma.bookTitle[ma.selectNo]
        textPublisher.text   = "サークル：" + ma.publisher[ma.selectNo]
        textAuthorName.text  = "作者　　：" + ma.authorName[ma.selectNo]
        textIssuedDate.text  = "発行日　：" + ma.issuedDate[ma.selectNo]
        textRecordDate.text  = "登録日　：" + ma.recordDate[ma.selectNo]
        textBasicGenre.text  = "ジャンル：" + ma.basicGenre[ma.selectNo]
        textPage.text        = "ページ数：" + ma.page[ma.selectNo]
        var tempText = "未入力"
        if(ma.rating[ma.selectNo] == 1) {
            tempText = "全年齢"
        }
        else if(ma.rating[ma.selectNo] == 2){
            tempText = "過激な表現あり"
        }
        else if(ma.rating[ma.selectNo] == 3){
            tempText = "成人向け"
        }
        else if(ma.rating[ma.selectNo] == 4){
            tempText = "グロテスク"
        }
        textRating.text = "対象年齢：" + tempText




    }
}