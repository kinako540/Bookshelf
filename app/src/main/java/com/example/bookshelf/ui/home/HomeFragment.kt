package com.example.bookshelf.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.example.bookshelf.CustomAdapter
import com.example.bookshelf.CustomAdapter2
import com.example.bookshelf.MainActivity
import com.example.bookshelf.R
import com.example.bookshelf.ui.create.CreateActivity
import com.example.bookshelf.ui.info.InfoActivity
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    var list = Array<String>(10) {"テキスト$it"}


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        if(MainActivity.selectViewType == 0){
            // 表示するテキスト配列を作る [テキスト１, テキスト２, ....]
            list = Array<String>(10) {"テキスト$it"}
            var adapter = CustomAdapter(list)
            // アダプターとレイアウトマネージャーをセット
            simpleRecyclerView.layoutManager = GridLayoutManager(requireContext(), 1)
            simpleRecyclerView.adapter = adapter
            simpleRecyclerView.setHasFixedSize(true)
            // インターフェースの実装
            adapter.setOnItemClickListener(object: CustomAdapter.OnItemClickListener{
                override fun onItemClickListener(view: View, position: Int, clickedText: String) {
                    Toast.makeText(requireContext(), "${clickedText}がタップされました", Toast.LENGTH_SHORT).show()
                    if(position == 0){
                        Toast.makeText(requireContext(), "こんにちは", Toast.LENGTH_SHORT).show()
                        val intent = Intent(requireContext(), InfoActivity::class.java)
                        startActivity(intent)
                    }
                    else{
                        Toast.makeText(requireContext(), "あああああ", Toast.LENGTH_SHORT).show()
                    }
                }
            })
        }
        if(MainActivity.selectViewType == 1){
            // 表示するテキスト配列を作る [テキスト１, テキスト２, ....]
            list = Array<String>(10) {"テキスト$it"}
            var adapter = CustomAdapter2(list)
            // アダプターとレイアウトマネージャーをセット
            simpleRecyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
            simpleRecyclerView.adapter = adapter
            simpleRecyclerView.setHasFixedSize(true)
            // インターフェースの実装
            adapter.setOnItemClickListener(object: CustomAdapter2.OnItemClickListener{
                override fun onItemClickListener(view: View, position: Int, clickedText: String) {
                    Toast.makeText(requireContext(), "${clickedText}がタップされました", Toast.LENGTH_SHORT).show()
                    if(position == 0){
                        Toast.makeText(requireContext(), "こんにちは", Toast.LENGTH_SHORT).show()
                    }
                    else{
                        Toast.makeText(requireContext(), "あああああ", Toast.LENGTH_SHORT).show()
                    }
                }
            })
        }




    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
        })
        return root
    }
}