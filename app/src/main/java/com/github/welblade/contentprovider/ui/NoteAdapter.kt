package com.github.welblade.contentprovider.ui

import android.database.Cursor
import android.provider.BaseColumns._ID
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.welblade.contentprovider.database.DbHelper.Companion.DESCRIPTION_NOTES
import com.github.welblade.contentprovider.database.DbHelper.Companion.TITLE_NOTES
import com.github.welblade.contentprovider.databinding.ItemNoteBinding

class NoteAdapter() : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {
    private var mCursor: Cursor? = null
    var cardClickListener: (Long) -> Unit = {}
    var deleteListener: (Long) -> Unit = {}
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val itemNoteBinding = ItemNoteBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return NoteViewHolder(itemNoteBinding)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        mCursor?.moveToPosition(position)
        val title = mCursor?.getString(
            mCursor?.getColumnIndex(TITLE_NOTES) as Int
        )
        val description = mCursor?.getString(
            mCursor?.getColumnIndex(DESCRIPTION_NOTES) as Int
        )
        val id = mCursor?.getLong(
            mCursor?.getColumnIndex(_ID) as Int
        ) as Long
        holder.apply {
            bind(title, description)
            setCardClickListener(id)
            setDeleteListener(id)
        }
    }

    override fun getItemCount(): Int {
        return mCursor?.count ?: 0
    }
    fun setCursor(newCursor: Cursor?){
        mCursor = newCursor
        notifyDataSetChanged()
    }
    inner class NoteViewHolder(private val item: ItemNoteBinding) : RecyclerView.ViewHolder(item.root){
        fun bind(title : String?, description: String? ){
            item.tvNoteTitle.text = title
            item.tvNoteDescription.text = description
        }
        fun setCardClickListener(id: Long){
            item.root.setOnClickListener {
                cardClickListener(id)
            }
        }
        fun setDeleteListener(id: Long){
            item.btnRemove.setOnClickListener {
                deleteListener(id)
            }
        }
    }
}

