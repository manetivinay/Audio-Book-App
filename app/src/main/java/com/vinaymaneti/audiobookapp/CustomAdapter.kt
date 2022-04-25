package com.vinaymaneti.audiobookapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView

class CustomAdapter(
    private val audioBooksList: MutableList<AudioBooksModel>,
    private val callbackInterface:CallbackInterface
) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

//    private var itemSelectionAction: ((AudioBooksModel) -> Unit, (Int) -> Unit)? = null

//
//    fun setItemSelectionAction(action: (AudioBooksModel) -> Unit, position: (Int) -> Unit) {
//        this.itemSelectionAction = action, pos
//    }


    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_view_design, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val audioBooksModel: AudioBooksModel = audioBooksList[position]

        // sets the text to the textview from our itemHolder class
        holder.bookTitleTextView.text = audioBooksModel.bookTitle
        holder.authorNameTextView.text = audioBooksModel.authorName
        holder.imageView.setImageBitmap(audioBooksModel.coverPhoto)
//        itemSelectionAction?.let{
            holder.itemRoot.setOnClickListener {
//                it(audioBooksModel, position)
                callbackInterface.passResultCallback(audioBooksModel, position)
            }
//        }

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return audioBooksList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imageView: ImageView = itemView.findViewById(R.id.cover_photo_image_view)
        val bookTitleTextView: TextView = itemView.findViewById(R.id.book_title)
        val authorNameTextView: TextView = itemView.findViewById(R.id.author_name)
        val itemRoot: ConstraintLayout = itemView.findViewById(R.id.item_root)

    }

    interface CallbackInterface {
        fun passResultCallback(audioBooksModel: AudioBooksModel, position: Int)
    }


}
