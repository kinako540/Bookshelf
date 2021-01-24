package com.example.bookshelf

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.nav_header_main.view.*
import kotlinx.android.synthetic.main.recyclerview_item.view.*
import kotlinx.android.synthetic.main.recyclerview_item.view.sampleImg
import kotlinx.android.synthetic.main.recyclerview_item_grid.view.*
import kotlinx.android.synthetic.main.recyclerview_item_grid.view.sampleTxt2 as sampleTxt21

//// customListはrecyclerViewのコンテンツとしてに表示するString配列のデータ
class CustomAdapter2(val customList: Array<String>) : RecyclerView.Adapter<CustomAdapter2.CustomViewHolder>(){

    // リスナー格納変数
    lateinit var listener: OnItemClickListener

    // ViewHolderクラス(別ファイルに書いてもOK)
    class CustomViewHolder(val view: View): RecyclerView.ViewHolder(view) {
    }

    // getItemCount onCreateViewHolder onBindViewHolderを実装
    // 上記のViewHolderクラスを使ってViewHolderを作成
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val item = layoutInflater.inflate(R.layout.recyclerview_item_grid, parent, false)
        return CustomViewHolder(item)
    }

    // recyclerViewのコンテンツのサイズ
    override fun getItemCount(): Int = MainActivity.maxNo

    // ViewHolderに表示する画像とテキストを挿入
    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.view.sampleImg2.setImageBitmap(MainActivity.bookimage[position])
        holder.view.sampleTxt2.text = MainActivity.bookTitle[position]
        //holder.view.sampleTxt2.text = "出版社：" + MainActivity.publisher[position]
        //holder.view.sampleTxt3.text = "作者　：" + MainActivity.authorName[position]
        //println("タイトル"+"["+ position +"]:"+ MainActivity.bookTitle[position])
        // タップしたとき
        holder.view.setOnClickListener {
            MainActivity.selectNo = position
            listener.onItemClickListener(it, position, MainActivity.bookTitle[position].toString())
            notifyDataSetChanged()
        }
    }

    //インターフェースの作成
    interface OnItemClickListener{
        fun onItemClickListener(view: View, position: Int, clickedText: String)
    }

    // リスナー
    fun setOnItemClickListener(listener: OnItemClickListener){
        this.listener = listener
    }


}
