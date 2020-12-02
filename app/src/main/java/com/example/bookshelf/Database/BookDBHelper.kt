package com.example.bookshelf.Database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.room.Database

class BookDBHelper(context: Context, databaseName:String, factory: SQLiteDatabase.CursorFactory?, version: Int):
                            SQLiteOpenHelper(context, databaseName, factory, version) {

    override fun onCreate(database: SQLiteDatabase?) {
        database?.execSQL("create table if not exists Book(id integer primary key, image None,title String, authorName String, publisher String, issuedDate String, recordDate String, page Int, basicGenre String, copyright Boolean, rating Int, favorite Int, describe String, possession Int, price Int, storageLocation String, saveDate Int)")
    }

    override fun onUpgrade(database: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (oldVersion < newVersion) {
            database?.execSQL("alter table SampleTable add column deleteFlag integer default 0")
        }
    }
}