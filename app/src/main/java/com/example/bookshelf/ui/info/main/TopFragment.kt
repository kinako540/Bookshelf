package com.example.bookshelf.ui.info.main

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.bookshelf.MainActivity
import com.example.bookshelf.R
import kotlinx.android.synthetic.main.top_fragment.*

class TopFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val ma = MainActivity
        val root = inflater.inflate(R.layout.top_fragment, container, false)
        val textSafeIcon :TextView = root.findViewById(R.id.textSafeIcon)
        val textSpicyIcon :TextView = root.findViewById(R.id.textSpicyIcon)
        val textR_18Icon :TextView = root.findViewById(R.id.textR_18Icon)
        val textGrotesqueIcon :TextView = root.findViewById(R.id.textGrotesqueIcon)
        val textNonIcon :TextView = root.findViewById(R.id.textNonIcon)
        val bookName: TextView = root.findViewById(R.id.bookName)

        //画像
        var img: ImageView = root.findViewById(R.id.BookView)
        img.setImageBitmap(ma.bookimage[ma.selectNo])

        //対象年齢
        if(ma.rating[ma.selectNo] == "全年齢対象") {
            textSafeIcon.setVisibility(View.VISIBLE)
            textSpicyIcon.setVisibility(View.GONE)
            textR_18Icon.setVisibility(View.GONE)
            textGrotesqueIcon.setVisibility(View.GONE)
            textNonIcon.setVisibility(View.GONE)
        }
        else if(ma.rating[ma.selectNo] == "春画"){
            textSafeIcon.setVisibility(View.GONE)
            textSpicyIcon.setVisibility(View.VISIBLE)
            textR_18Icon.setVisibility(View.GONE)
            textGrotesqueIcon.setVisibility(View.GONE)
            textNonIcon.setVisibility(View.GONE)
        }
        else if(ma.rating[ma.selectNo] == "R-18"){
            textSafeIcon.setVisibility(View.GONE)
            textSpicyIcon.setVisibility(View.GONE)
            textR_18Icon.setVisibility(View.VISIBLE)
            textGrotesqueIcon.setVisibility(View.GONE)
            textNonIcon.setVisibility(View.GONE)
        }
        else if(ma.rating[ma.selectNo] == "R-18G"){
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

        bookName.text = ma.bookTitle[ma.selectNo]

        return root
    }

}