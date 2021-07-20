package com.github.welblade.contentprovider.ui

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import com.github.welblade.contentprovider.databinding.FragmentNoteDetailBinding

class NoteDetailFragment: DialogFragment(), DialogInterface.OnClickListener {
    private lateinit var fragmentNoteDetailBinding: FragmentNoteDetailBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater =  activity?.layoutInflater as LayoutInflater
        fragmentNoteDetailBinding = FragmentNoteDetailBinding.inflate(
           inflater,
            null,
            false
        )
        return super.onCreateDialog(savedInstanceState)
    }
    override fun onClick(dialog: DialogInterface?, which: Int) {
        TODO("Not yet implemented")
    }
}