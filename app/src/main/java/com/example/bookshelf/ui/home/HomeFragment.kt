package com.example.bookshelf.ui.home

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.ToggleButton
import androidx.activity.addCallback
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.bookshelf.CustomAdapter
import com.example.bookshelf.CustomAdapter2
import com.example.bookshelf.MainActivity
import com.example.bookshelf.MainActivity.Companion.searchBar
import com.example.bookshelf.R
import com.example.bookshelf.ui.create.CreateActivity
import com.example.bookshelf.ui.gallery.GalleryFragment
import com.example.bookshelf.ui.info.InfoActivity
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    var list = Array<String>(10) {"テキスト$it"}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    var deleteNo :String = "0"


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        if(searchBar){
            search.visibility = View.VISIBLE
        }

        //戻るイベント
        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            shutdownLog()
        }

        hideBottomNav()
        hideBottomNav2()

        var selectBtn = false
        fab.setOnClickListener {
            if(selectBtn) {
                hideBottomNav()
                hideBottomNav2()
                selectBtn = false
            }
            else{
                showBottomNav()
                showBottomNav2()
                selectBtn = true
            }
        }
        val gf = GalleryFragment()
        val ma = MainActivity()
        fab2.setOnClickListener {
            val intent = Intent(requireContext(), CreateActivity::class.java)
            startActivity(intent)
        }
        fab3.setOnClickListener {
            MainActivity.selectCameraIntent = true
            val secondFragment = GalleryFragment()
            val fragmentTransaction = fragmentManager?.beginTransaction()
            fragmentTransaction?.addToBackStack(null)
            fragmentTransaction?.replace(R.id.nav_host_fragment, secondFragment)
            fragmentTransaction?.commit()
        }
        fab4.setOnClickListener {
            MainActivity.selectChooseImageIntent = true
            val secondFragment = GalleryFragment()
            val fragmentTransaction = fragmentManager?.beginTransaction()
            fragmentTransaction?.addToBackStack(null)
            fragmentTransaction?.replace(R.id.nav_host_fragment, secondFragment)
            fragmentTransaction?.commit()
        }



        if(MainActivity.selectViewType == 0){
            // 表示するテキスト配列を作る [テキスト１, テキスト２, ....]
            list = Array<String>(10) {"テキスト$it"}
            var adapter = CustomAdapter(list)
            // アダプターとレイアウトマネージャーをセット
            simpleRecyclerView.layoutManager = GridLayoutManager(requireContext(), 1)
            simpleRecyclerView.adapter = adapter
            simpleRecyclerView.setHasFixedSize(true)
            // インターフェースの実装
            adapter.setOnItemClickListener(object : CustomAdapter.OnItemClickListener {
                override fun onItemClickListener(view: View, position: Int, clickedText: String) {
                    //MainActivity.selectNo = position
                    Toast.makeText(requireContext(), "${clickedText}がタップされました", Toast.LENGTH_SHORT)
                        .show()
                    val intent = Intent(requireContext(), InfoActivity::class.java)
                    startActivity(intent)
                }

                override fun onItemLongClickListener(
                    view: View,
                    position: Int,
                    clickedText: String
                ) {
                    deleteNo = MainActivity.bookID[MainActivity.selectNo].toString()
                    Toast.makeText(requireContext(), "${clickedText}が長押しされました", Toast.LENGTH_SHORT)
                        .show()
                    deleteLog()
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
            adapter.setOnItemClickListener(object : CustomAdapter2.OnItemClickListener {
                override fun onItemClickListener(view: View, position: Int, clickedText: String) {
                    //MainActivity.selectNo = position
                    Toast.makeText(requireContext(), "${clickedText}がタップされました", Toast.LENGTH_SHORT)
                        .show()
                    val intent = Intent(requireContext(), InfoActivity::class.java)
                    startActivity(intent)
                }
            })
        }
        /*if(MainActivity.selectViewType == 1){
            // 表示するテキスト配列を作る [テキスト１, テキスト２, ....]
            list = Array<String>(10) {"テキスト$it"}
            var adapter = CustomAdapter2(list)
            // アダプターとレイアウトマネージャーをセット
            simpleRecyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
            simpleRecyclerView.adapter = adapter
            simpleRecyclerView.setHasFixedSize(true)
            // インターフェースの実装
            adapter.setOnItemClickListener(object : CustomAdapter2.OnItemClickListener {
                override fun onItemClickListener(view: View, position: Int, clickedText: String) {
                    Toast.makeText(requireContext(), "${clickedText}がタップされました", Toast.LENGTH_SHORT)
                        .show()
                    val intent = Intent(requireContext(), InfoActivity::class.java)
                    startActivity(intent)
                }
            })
        }*/
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val view: ToggleButton = root.findViewById(R.id.fab)
        view.setOnCheckedChangeListener{ _, a->
            if(a) {
                view.background = getDrawable(requireContext(), R.drawable.cross)
            }
            else{
                view.background = getDrawable(requireContext(), R.drawable.plus)
            }
        }
        return root
    }

    private fun showBottomNav() {
        fab2.visibility = View.VISIBLE
        fab3.visibility = View.VISIBLE
        fab4.visibility = View.VISIBLE
    }
    private fun showBottomNav2() {
        textSyudou.visibility = View.VISIBLE
        textCamera.visibility = View.VISIBLE
        textImage.visibility = View.VISIBLE
    }

    private fun hideBottomNav() {
        fab2.visibility = View.GONE
        fab3.visibility = View.GONE
        fab4.visibility = View.GONE
    }
    private fun hideBottomNav2() {
        textSyudou.visibility = View.GONE
        textCamera.visibility = View.GONE
        textImage.visibility = View.GONE
    }
    fun deleteLog(){
        AlertDialog.Builder(requireContext()) // FragmentではActivityを取得して生成
            .setTitle("削除")
            .setNegativeButton("キャンセル", null)
            .setMessage("この作品を削除しますか？")
            .setPositiveButton("OK") { dialog, which ->
                (activity as MainActivity)?.deleteData(deleteNo)
                println("削除されたID：" + deleteNo)
                println("削除された本のタイトル：" + MainActivity.bookTitle[deleteNo.toInt()])
            }
            .show()
    }
    fun shutdownLog(){
        AlertDialog.Builder(requireContext()) // FragmentではActivityを取得して生成
            .setTitle("ホーム")
            .setNegativeButton("キャンセル", null)
            .setMessage("ホームに戻りますか？")
            .setPositiveButton("OK") { dialog, which ->
                //activity?.finishAndRemoveTask()
                val homeIntent = Intent(Intent.ACTION_MAIN)
                homeIntent.addCategory(Intent.CATEGORY_HOME)
                homeIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                this.startActivity(homeIntent)
            }
            .show()
    }
}
