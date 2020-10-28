package com.example.bookshelf.Database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [books::class], version = 1)
abstract class BookDatabase : RoomDatabase() {

}