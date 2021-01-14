package com.example.bookshelf

import android.app.AlertDialog
import android.app.PendingIntent.getActivity
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.nav_header_main.view.*
import kotlinx.android.synthetic.main.recyclerview_item.view.*


//// customListはrecyclerViewのコンテンツとしてに表示するString配列のデータ
class CustomAdapter(val customList: Array<String>) : RecyclerView.Adapter<CustomAdapter.CustomViewHolder>(){

    // リスナー格納変数
    lateinit var listener: OnItemClickListener

    // ViewHolderクラス(別ファイルに書いてもOK)
    class CustomViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        val sampleImg = view.sampleImg
        val sampleTxt = view.sampleTxt
        val sampleTxt2 = view.sampleTxt2
    }

    // getItemCount onCreateViewHolder onBindViewHolderを実装
    // 上記のViewHolderクラスを使ってViewHolderを作成
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val item = layoutInflater.inflate(R.layout.recyclerview_item, parent, false)
        return CustomViewHolder(item)
    }

    // recyclerViewのコンテンツのサイズ
    override fun getItemCount(): Int = MainActivity.maxNo

    // ViewHolderに表示する画像とテキストを挿入
    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {


        holder.view.sampleImg_x.setImageBitmap(MainActivity.bookimage[position])
        println("ポジション:$position")
        holder.view.sampleTxt.text = MainActivity.bookTitle[position]
        holder.view.sampleTxt2.text = "" + MainActivity.authorName[position]
        holder.view.sampleTxt3.text = "出版社　：" + MainActivity.publisher[position]
        holder.view.sampleTxt4.text = "ジャンル：" + MainActivity.basicGenre[position]
        holder.view.sampleTxt5.text = "対象年齢：" + MainActivity.rating[position]
        println("タイトル" + "[" + position + "]:" + MainActivity.bookTitle[position])
        // タップしたとき
        holder.view.setOnClickListener {
            MainActivity.selectNo = position
            listener.onItemClickListener(it, position, MainActivity.bookTitle[position].toString())
            notifyDataSetChanged()
        }
        holder.view.setOnLongClickListener {
            MainActivity.selectNo = position
            listener.onItemLongClickListener(it, position, MainActivity.bookTitle[position].toString())
            true
        }
    }

    //インターフェースの作成
    interface OnItemClickListener{
        fun onItemClickListener(view: View, position: Int, clickedText: String)
        fun onItemLongClickListener(view: View, position: Int, clickedText: String)
    }

    // リスナー
    fun setOnItemClickListener(listener: OnItemClickListener){
        this.listener = listener
    }
    fun setOnItemLongClickListener(listener: OnItemClickListener){
        this.listener = listener
    }


}