package com.example.workdiary.SQLite

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(
    context: Context?,
    name: String?,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
) : SQLiteOpenHelper(context, name, factory, version) {

    private val TABLE_NAME = "worktable"

    override fun onCreate(db: SQLiteDatabase) {
        var tableCreateQuery  = "CREATE TABLE if not exists $TABLE_NAME (" +
                "wId integer primary key autoincrement, " +
                "wTitle text, "+
                "wSetName text, "+
                "wDate text, "+
                "wStartTime text, "+
                "wEndTime text, "+
                "wMoney integer, "+
                "wIsDone integer "+
                ");";
        db.execSQL(tableCreateQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        val sql : String = "DROP TABLE if exists $TABLE_NAME"
        db.execSQL(sql)
        onCreate(db)
    }

}