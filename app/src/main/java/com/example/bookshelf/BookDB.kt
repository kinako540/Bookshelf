package com.example.bookshelf

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class bookdb(
    @PrimaryKey(autoGenerate = true)val id: Int,
    var bookTitle: String,
    var authorName: String?,
    var publisher: String?,
    var issuedDate: String?,
    var recordDate: String?,
    var page: Int?,
    var basicGenre :String?,
    var copyright:Boolean?,
    var rating : Int?,
    var favorite: Int?,
    //var userTag:
    var describe: String?,
    var possession: Int?,
    var price: Int?,
    var storageLocation: String?,
    var saveDate:Boolean?
)