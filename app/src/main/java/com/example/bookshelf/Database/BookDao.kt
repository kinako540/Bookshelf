package com.example.bookshelf.Database

import androidx.room.*

@Dao
interface BookDao {
    @Query("SELECT * FROM Book")
    fun getAll(): List<Book>

    @Insert
    fun insert(book: Book)

    @Update
    fun update(book: Book)

    @Delete
    fun delete(book: Book)

}