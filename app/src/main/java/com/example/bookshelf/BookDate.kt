package com.example.bookshelf

import android.app.Application
import java.util.*


//==================================================================================================================
// 変数定義書　：　https://docs.google.com/spreadsheets/d/1QVHmUPHnQCWAPfo0-dy4t66IXDP0l4cNlkYnfnaBJQM/edit?usp=sharing
//==================================================================================================================

public class BookDate : Application(){
    var userTag: Array<Array<String>?> = arrayOfNulls(10000)

    companion object {
        var no: Array<Int?> = arrayOfNulls(10000)
        var title: Array<String?> = arrayOfNulls(10000)
        var authorName: Array<String?> = arrayOfNulls(10000)
        var publisher: Array<String?> = arrayOfNulls(10000)
        var issuedDate: Array<Date?> = arrayOfNulls(10000)
        var recordDate: Array<Date?> = arrayOfNulls(10000)
        var page: Array<Int?> = arrayOfNulls(10000)
        var basicGenre: Array<String?> = arrayOfNulls(10000)
        var copyright: Array<Boolean> = arrayOf(false)
        var rating: Array<Int?> = arrayOfNulls(10000)
        var favorite: Array<Boolean> = arrayOf(false)
        var userTag: Array<Array<String>?> = arrayOfNulls(10000)
        var describe: Array<String?> = arrayOfNulls(10000)
        var possession: Array<Int?> = arrayOfNulls(10000)
        var price: Array<Int?> = arrayOfNulls(10000)
        var storageLocation: Array<String?> = arrayOfNulls(10000)
        var getLocation: Array<String?> = arrayOfNulls(10000)
    }




}