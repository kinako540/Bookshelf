package com.example.bookshelf.Database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface BookDao {
    @Query("SELECT * FROM Book ORDER BY id ASC")//本全部取得
    fun getAllBooks(): LiveData<List<Book>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)//本追加
    fun insert(book: Book)

    @Update
    fun update(book: Book)//更新

    //@Delete
    //fun delite()


}