package com.example.bookshelf.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.bookshelf.CustomAdapterLink
import com.example.bookshelf.MainActivity
import com.example.bookshelf.R
import com.example.bookshelf.ui.info.InfoActivity
import kotlinx.android.synthetic.main.fragment_slideshow.*


class SlideshowFragment : Fragment() {

    var list = Array<String>(10) {"テキスト$it"}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    var deleteNo :String = "0"


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

            // 表示するテキスト配列を作る [テキスト１, テキスト２, ....]
            list = Array<String>(10) {"テキスト$it"}
            var adapter = CustomAdapterLink(list)
            // アダプターとレイアウトマネージャーをセット
            simpleRecyclerView2.layoutManager = GridLayoutManager(requireContext(), 1)
            simpleRecyclerView2.adapter = adapter
            simpleRecyclerView2.setHasFixedSize(true)
            // インターフェースの実装
            adapter.setOnItemClickListener(object : CustomAdapterLink.OnItemClickListener {
                override fun onItemClickListener(view: View, position: Int, clickedText: String) {
                    Toast.makeText(requireContext(), "${clickedText}がタップされました", Toast.LENGTH_SHORT)
                        .show()
                    val intent = Intent(requireContext(), InfoActivity::class.java)
                    startActivity(intent)
                }
            })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_slideshow, container, false)
        return root
    }
}
