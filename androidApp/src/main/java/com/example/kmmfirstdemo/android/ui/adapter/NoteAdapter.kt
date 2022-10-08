package com.example.kmmfirstdemo.android.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.kmmfirstdemo.android.databinding.NoteItemBinding
import note.Note

class NoteAdapter(val onSelectNoteId: (id: Long) -> Unit) :
    ListAdapter<Note, NoteAdapter.NoteViewHolder>(NoteDiffCallback) {

    private val noteList = mutableListOf<Note>()

    class NoteViewHolder(val binding: NoteItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
            NoteItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = noteList[position]
        holder.binding.apply {
            tvContent.text = note.content
            tvTitle.text = note.title
            tvLastEditTime.text = note.lastEditTime
            noteItemLayout.setOnClickListener {
                onSelectNoteId(note.id)
            }
        }
    }

    override fun getItemCount(): Int {
        return noteList.size
    }

    fun loadData(data: List<Note>) {
        noteList.clear()
        noteList.addAll(data)
        notifyDataSetChanged()
    }

}

object NoteDiffCallback : DiffUtil.ItemCallback<Note>() {
    override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
        return oldItem.id == newItem.id
    }
}
