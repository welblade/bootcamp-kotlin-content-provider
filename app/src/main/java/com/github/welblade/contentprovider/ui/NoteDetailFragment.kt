package com.github.welblade.contentprovider.ui

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.ContentValues
import android.content.DialogInterface
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import com.github.welblade.contentprovider.R
import com.github.welblade.contentprovider.database.DbHelper.Companion.DESCRIPTION_NOTES
import com.github.welblade.contentprovider.database.DbHelper.Companion.TITLE_NOTES
import com.github.welblade.contentprovider.database.NotesProvider.Companion.NOTES_URI
import com.github.welblade.contentprovider.databinding.FragmentNoteDetailBinding

class NoteDetailFragment: DialogFragment(), DialogInterface.OnClickListener {
    private lateinit var fragmentNoteDetailBinding: FragmentNoteDetailBinding
    private var id: Long = 0

    companion object {
        private const val EXTRA_ID = "id"
        fun newInstance(id: Long): NoteDetailFragment{
            val bundle = Bundle()
            bundle.putLong(EXTRA_ID, id)
            return NoteDetailFragment().apply{
                arguments = bundle
            }
        }
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater =  activity?.layoutInflater as LayoutInflater
        fragmentNoteDetailBinding = FragmentNoteDetailBinding.inflate(
           inflater,
            null,
            false
        )
        var dialogTitle = getString(R.string.new_note)
        if(arguments != null && (arguments?.getLong(EXTRA_ID) as Long) > 0L){
            id = arguments?.getLong(EXTRA_ID) as Long
            val uri = Uri.withAppendedPath(NOTES_URI, id.toString())
            val cursor = activity?.contentResolver?.query(
                uri,
                null,
                null,
                null,
                null
            )
            if(cursor?.moveToNext() as Boolean){
                dialogTitle = getString(R.string.edit_note)
                val title = cursor.getString(cursor.getColumnIndex(TITLE_NOTES))
                val description = cursor.getString(cursor.getColumnIndex(DESCRIPTION_NOTES))
                setupFields(title, description)
            }
            cursor.close()
        }
        return AlertDialog.Builder(activity as Activity)
            .setTitle(dialogTitle)
            .setView(fragmentNoteDetailBinding.root)
            .setPositiveButton(getString(R.string.save), this)
            .setNegativeButton(getString(R.string.cancel), this)
            .create()
    }
    override fun onClick(dialog: DialogInterface?, which: Int) {
        val values = ContentValues()
        values.put(TITLE_NOTES, fragmentNoteDetailBinding.edtTitle.text.toString())
        values.put(DESCRIPTION_NOTES, fragmentNoteDetailBinding.edtDescription.text.toString())

        if(id != 0L){
            val uri = Uri.withAppendedPath(NOTES_URI, id.toString())
            context?.contentResolver?.update(uri, values, null, null)
        }else{
            context?.contentResolver?.insert(NOTES_URI, values)
        }
    }
    private fun setupFields(title: String, description: String){
        fragmentNoteDetailBinding.edtTitle.setText(title)
        fragmentNoteDetailBinding.edtDescription.setText(description)
    }
}