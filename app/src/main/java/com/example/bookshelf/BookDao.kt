package com.example.bookshelf

import androidx.room.*

@Dao
interface BookDao {
    @Query("SELECT * FROM bookdb")
    fun getAll(): List<bookdb>

    @Insert
    fun insert(user: bookdb)

    @Update
    fun update(user: bookdb)

    @Delete
    fun delete(user: bookdb)
}