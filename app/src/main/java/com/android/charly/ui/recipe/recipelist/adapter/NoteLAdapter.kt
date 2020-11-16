package com.android.charly.ui.recipe.recipelist.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.*
import com.android.charly.R
import com.android.charly.persistence.Note
import kotlinx.android.synthetic.main.note_items.view.*



class NoteAdapter(
    noteList: List<Note>
) : RecyclerView.Adapter<NoteAdapter.ViewHolder>() {

    private val notes = mutableListOf<Note>()


    init {
        notes.addAll(noteList)
    }

    // Method #1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.note_items, parent, false)
        return ViewHolder(
            view)
    }

    // Method #2
    override fun getItemCount() = notes.size


    // Method #3
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(item = notes[position])
    }


    // Method #4
    fun swap(notes: List<Note>) {
        val diffCallback = DiffCallback(this.notes, notes)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        this.notes.clear()
        this.notes.addAll(notes)
        diffResult.dispatchUpdatesTo(this)
    }


    // Method #5
    class ViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
         val viewPool = RecyclerView.RecycledViewPool()
        // Method #6
        fun bind(item: Note) {
            itemView.txtTitle.text = item.title
            itemView.txtDescription.text = item.description
            itemView.recycle_note_image.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                adapter = item.image?.let { ChildAdapter(it) }


            }
        }

    }

}