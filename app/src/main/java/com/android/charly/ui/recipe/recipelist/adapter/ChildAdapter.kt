package com.android.charly.ui.recipe.recipelist.adapter

import android.graphics.BitmapFactory
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.android.charly.R
import kotlinx.android.synthetic.main.horizontal_note_item.view.*

class ChildAdapter(private val children : MutableList<String?>)
    : RecyclerView.Adapter<ChildAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =  LayoutInflater.from(parent.context)
                .inflate(R.layout.horizontal_note_item,parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return children.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val child = children[position]
        if (!TextUtils.isEmpty(child)) {
            holder.imageView.setImageBitmap(BitmapFactory.decodeFile(child))
        }
    }
    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val imageView: ImageView = itemView.img_note_recycle

    }
}