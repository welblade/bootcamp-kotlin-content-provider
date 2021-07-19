package com.github.welblade.contentprovider.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns._ID

class DbHelper(context: Context): SQLiteOpenHelper(context, DB_NAME, null, VERSION) {
    companion object{
        const val DB_NAME = "notes.db"
        const val VERSION = 1
        const val TABLE_NOTES: String = "Notes"
        const val TITLE_NOTES: String = "title"
        const val DESCRIPTION_NOTES: String = "description"
    }
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("""
            CREATE TABLE $TABLE_NOTES (
                $_ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                $TITLE_NOTES TEXT NOT NULL,
                $DESCRIPTION_NOTES TEXT NOT NULL)
            """.trimIndent())
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

}