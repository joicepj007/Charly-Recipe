package com.android.charly.ui.recipe.recipedetail.adapter

import android.graphics.BitmapFactory
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import com.android.charly.R
import java.util.*

class RecipeImageRecycleAdapter(private val callbackInterface: CallbackInterface) : RecyclerView.Adapter<RecyclerView.ViewHolder?>() {
    private val img: MutableList<String?>
    private var selectedPosition = -1
    private var imageRecycleClick: ImageRecycleClick? = null
    private var oldPosition = 0

    interface ImageRecycleClick {
        fun onItemClick(view: View?, position: Int)
        fun onDeleteClick(view: View?, position: Int)
        fun onimagePickerClick()
    }

    fun addData(imagePath: String?) {
        img.add(imagePath)
        notifyItemInserted(img.size - 1)
    }

    fun deleteImage(position: Int) {
        img.removeAt(position)
        notifyItemRemoved(position)
    }

    fun setImageRecycleClick(imageRecycleClick: ImageRecycleClick?) {
        this.imageRecycleClick = imageRecycleClick
    }

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view),
        OnLongClickListener {
        var image: ImageView
        var imageDelete: ImageView
        var relativeLayout: RelativeLayout
        fun bind(imagePath: String?) {
            if (!TextUtils.isEmpty(imagePath)) {
                image.setImageBitmap(BitmapFactory.decodeFile(imagePath))
            }
            if (selectedPosition == getAdapterPosition()) {
                relativeLayout.visibility = View.VISIBLE
            } else {
                relativeLayout.visibility = View.INVISIBLE
            }
            itemView.setOnClickListener(View.OnClickListener { v ->
                changePosition(getAdapterPosition())
                imageRecycleClick?.onItemClick(v, getAdapterPosition())
            })
            imageDelete.setOnClickListener { v ->
                imageRecycleClick?.onDeleteClick(
                    v,
                    getAdapterPosition()
                )
            }
            itemView.setOnLongClickListener(this)
        }

        override fun onLongClick(view: View): Boolean {
            return false
        }

        init {
            image =
                view.findViewById<View>(R.id.img_recycle) as ImageView
            imageDelete =
                view.findViewById<View>(R.id.img_delete) as ImageView
            relativeLayout =
                view.findViewById<View>(R.id.selected_img_bg) as RelativeLayout
        }
    }

    inner class ImageViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {
        var imageEdit: ImageView
        var footerLayout: RelativeLayout
        fun bind() {
            itemView.setOnClickListener(View.OnClickListener { imageRecycleClick?.onimagePickerClick() })
        }

        init {
            imageEdit =
                view.findViewById<View>(R.id.img_edit) as ImageView
            footerLayout =
                view.findViewById<View>(R.id.layout_footer) as RelativeLayout
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_FOOTER) {
            val v = LayoutInflater.from(parent.context).inflate(
                R.layout.recycle_image_layout,
                parent, false
            )
            ImageViewHolder(v)
        } else if (viewType == TYPE_ITEM) {
            val v = LayoutInflater.from(parent.context).inflate(
                R.layout.recycle_image_item,
                parent, false
            )
            MyViewHolder(v)
        } else {
            throw IllegalArgumentException("Invalid View Type")
        }
    }



    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MyViewHolder) {
            (holder as MyViewHolder).bind(img[position])
        } else if (holder is ImageViewHolder) {
            (holder as ImageViewHolder).bind()
        }
        callbackInterface.passDataCallback(img)
    }



    fun changePosition(position: Int) {
        oldPosition = selectedPosition
        selectedPosition = position
        notifyItemChanged(selectedPosition)
        notifyItemChanged(oldPosition)
    }

    override fun getItemViewType(position: Int): Int {
        return if (isPositionFooter(position)) {
            TYPE_FOOTER
        } else TYPE_ITEM
    }

    private fun isPositionFooter(position: Int): Boolean {
        return position == itemCount - 1
    }

    companion object {
        const val TYPE_ITEM = 0
        const val TYPE_FOOTER = 1
    }

    init {
        img = ArrayList()
    }

    override fun getItemCount(): Int {
        return img.size + 1
    }

    interface CallbackInterface {
        fun passDataCallback(img: MutableList<String?>)
    }

}