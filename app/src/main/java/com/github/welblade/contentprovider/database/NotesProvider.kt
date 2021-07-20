package com.github.welblade.contentprovider.database

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.media.UnsupportedSchemeException
import android.net.Uri
import android.provider.BaseColumns._ID
import com.github.welblade.contentprovider.database.DbHelper.Companion.TABLE_NOTES

class NotesProvider : ContentProvider() {

    val uriMacther: UriMatcher by lazy {
        UriMatcher(UriMatcher.NO_MATCH).apply{
            addURI(AUTHORITY, "notes", NOTES)
            addURI(AUTHORITY, "notes/#", NOTES_BY_ID)
        }
    }
    private lateinit var dbHelper: DbHelper

    override fun onCreate(): Boolean {
        if(context != null) {
            dbHelper = DbHelper(context as Context)
        }
        return true
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        if(uriMacther.match(uri) == NOTES_BY_ID){
            val db: SQLiteDatabase = dbHelper.writableDatabase
            val affectedLines = db.delete(TABLE_NOTES, "$_ID = ?", arrayOf(uri.lastPathSegment))
            //db.close()
            context?.contentResolver?.notifyChange(uri, null)
            return affectedLines
        }else{
            throw UnsupportedSchemeException("Invalid URI for exclusion.")
        }
    }

    override fun getType(uri: Uri): String? = throw UnsupportedSchemeException("Not implemented URI.")

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        if(uriMacther.match(uri) == NOTES) {
            val db: SQLiteDatabase = dbHelper.writableDatabase
            val id = db.insert(TABLE_NOTES, null, values)
            val insertUri = Uri.withAppendedPath(BASE_URI, id.toString())
            //db.close()
            context?.contentResolver?.notifyChange(uri, null)
            return insertUri
        }else{
            throw UnsupportedSchemeException("Invalid Uri to insertion.")
        }
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        return when {
            uriMacther.match(uri) == NOTES -> {
                val db: SQLiteDatabase = dbHelper.readableDatabase
                val cursor = db.query(TABLE_NOTES, projection, selection, selectionArgs, null, null, sortOrder)

                cursor.setNotificationUri(context?.contentResolver, uri)
                //db.close()
                cursor
            }
            uriMacther.match(uri) == NOTES_BY_ID -> {
                val db: SQLiteDatabase = dbHelper.readableDatabase
                val cursor = db.query(TABLE_NOTES, projection, "$_ID = ?", arrayOf(uri.lastPathSegment), null, null, sortOrder)

                cursor.setNotificationUri(context?.contentResolver, uri)
                //db.close()
                cursor
            }
            else -> {
                throw UnsupportedSchemeException("Not implemented URI.")
            }
        }
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        if(uriMacther.match(uri) == NOTES_BY_ID){
            val db: SQLiteDatabase = dbHelper.writableDatabase
            val affectedLines = db.update(TABLE_NOTES, values, "$_ID = ?", arrayOf(uri.lastPathSegment))

            context?.contentResolver?.notifyChange(uri, null)
            //db.close()
            return affectedLines
        }else{
            throw UnsupportedSchemeException("Invalid Uri to update.")
        }
    }

    companion object{
        const val AUTHORITY = "com.github.welblade.contentprovider.provider"
        val BASE_URI = Uri.parse("content://$AUTHORITY")
        const val NOTES = 1
        val NOTES_URI = Uri.withAppendedPath(BASE_URI, "notes")
        const val NOTES_BY_ID = 2
        val NOTES_BY_ID_URI = Uri.withAppendedPath(BASE_URI, "notes/#")
    }
}