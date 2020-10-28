package com.example.bookshelf.Database

import androidx.room.*

@Dao
interface BookDao {
    @Query("SELECT * FROM books")
    fun getAll(): List<books>

    @Insert
    fun insert(user: books)

    @Update
    fun update(user: books)

    @Delete
    fun delete(user: books)
}