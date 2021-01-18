package com.example.bookshelf

import android.app.PendingIntent.getActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.recyclerview_item.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

//// customListはrecyclerViewのコンテンツとしてに表示するString配列のデータ
class CustomAdapterLink(val customList: Array<String>) : RecyclerView.Adapter<CustomAdapterLink.CustomViewHolder>(){

    // リスナー格納変数
    lateinit var listener: OnItemClickListener

    // ViewHolderクラス(別ファイルに書いてもOK)
    class CustomViewHolder(val view: View): RecyclerView.ViewHolder(view) {
    }

    // getItemCount onCreateViewHolder onBindViewHolderを実装
    // 上記のViewHolderクラスを使ってViewHolderを作成
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val item = layoutInflater.inflate(R.layout.recyclerview_item_link, parent, false)
        return CustomViewHolder(item)
    }

    // recyclerViewのコンテンツのサイズ
    override fun getItemCount(): Int = 1

    // ViewHolderに表示する画像とテキストを挿入
    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        //holder.view.sampleImg_x.setImageResource(R.mipmap.ic_launcher_round)
        holder.view.sampleTxt.text = MainActivity.linkName[MainActivity.selectNo]
        holder.view.sampleTxt2.text = MainActivity.linkURL[MainActivity.selectNo]
        //holder.view.sampleTxt2.text = "出版社：" + MainActivity.publisher[position]
        //holder.view.sampleTxt3.text = "作者　：" + MainActivity.authorName[position]
        //println("タイトル"+"["+ position +"]:"+ MainActivity.bookTitle[position])
        // タップしたとき
        holder.view.setOnClickListener {
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