package com.github.welblade.contentprovider

import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.welblade.contentprovider.database.DbHelper.Companion.TITLE_NOTES
import com.github.welblade.contentprovider.database.NotesProvider.Companion.NOTES_URI
import com.github.welblade.contentprovider.databinding.ActivityMainBinding
import com.github.welblade.contentprovider.ui.NoteAdapter

class MainActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<Cursor> {
    private val activityMainBinding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val adapter: NoteAdapter by lazy{
        NoteAdapter().apply {
            setHasStableIds(true)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityMainBinding.root)
        activityMainBinding.rvNotes.layoutManager = LinearLayoutManager(this)
        activityMainBinding.rvNotes.adapter = adapter
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        return CursorLoader(
            this,
            NOTES_URI,
            null,
            null,
            null,
            TITLE_NOTES)
    }

    private fun setListeners(){
        adapter.cardClickListener = {

        }
        adapter.deleteListener = {
            contentResolver.delete(
                Uri.withAppendedPath(NOTES_URI, it.toString())
                , null,
                null
            )
        }
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        TODO("Not yet implemented")
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        TODO("Not yet implemented")
    }
}