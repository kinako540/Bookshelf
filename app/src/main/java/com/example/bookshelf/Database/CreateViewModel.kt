package com.example.bookshelf.Database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class CreateViewModel(app: Application): AndroidViewModel(app) {

    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main

    private val  scope = CoroutineScope(coroutineContext)
    // データ操作用のリポジトリクラス
    private val repository: BookRepository

    // 全データリスト
    val allBooks: LiveData<List<Book>>

    init {
        val bookDao = BookDatabase.getDatabase(app).bookDao()
        repository = BookRepository(bookDao)
        allBooks = repository.allBooks
    }

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }

    fun insert(book: Book) = scope.launch(Dispatchers.IO){
        repository.insert(book)
    }


}