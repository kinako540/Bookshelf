package com.example.bookshelf.ui.info.main

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.bookshelf.MainActivity
import com.example.bookshelf.R

class TextFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val ma = MainActivity
        val root = inflater.inflate(R.layout.fragment_text, container, false)
        val textTitle: TextView = root.findViewById(R.id.textTitle)
        val textPublisher: TextView = root.findViewById(R.id.textPublisher)
        val textAuthorName: TextView = root.findViewById(R.id.textAuthorName)
        val textIssuedDate: TextView = root.findViewById(R.id.textIssuedDate)
        val textRecordDate: TextView = root.findViewById(R.id.textRecordDate)
        val textBasicGenre: TextView = root.findViewById(R.id.textBasicGenre)
        val textPage: TextView = root.findViewById(R.id.textPage)
        val textRating: TextView = root.findViewById(R.id.textRating)
        val textPosition: TextView = root.findViewById(R.id.textPossession)
        val textRede2: TextView = root.findViewById(R.id.textRede2)

        textTitle.text = "タイトル：" + ma.bookTitle[ma.selectNo]
        textPublisher.text   = "サークル：" + ma.publisher[ma.selectNo]
        textAuthorName.text  = "作者　　：" + ma.authorName[ma.selectNo]
        textIssuedDate.text  = "発行日　：" + ma.issuedDate[ma.selectNo]
        textRecordDate.text  = "登録日　：" + ma.recordDate[ma.selectNo]
        textBasicGenre.text  = "ジャンル：" + ma.basicGenre[ma.selectNo]
        textPage.text        = "ページ数：" + ma.page[ma.selectNo]
        textRating.text      = "対象年齢：" + ma.rating[ma.selectNo]
        textPosition.text    = "所持状況：" + ma.possession[ma.selectNo]
        textRede2.text       = ma.describe[ma.selectNo]
        return root
    }

}