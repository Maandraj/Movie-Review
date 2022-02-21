package com.example.moviereview.features.review.presentation.favourite.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.example.moviereview.R
import com.example.moviereview.databinding.FavouriteItemBinding
import com.example.moviereview.features.review.domain.model.room.Favourites
import com.example.moviereview.features.review.presentation.interfaces.ClickRemoveListener


class FavouriteAdapter(
    private val favourites: MutableList<Favourites>,
    private val clickListener: ClickRemoveListener
) :
    RecyclerView.Adapter<FavouriteAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding: FavouriteItemBinding by viewBinding(FavouriteItemBinding::bind)
        val title = binding.tvTitle
        val image = binding.ivImage
        val description = binding.tvDescription
        val publishDate = binding.tvPublishDate
        val readBtn = binding.btnRead
        val removeIv = binding.ivRemove
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
           LayoutInflater.from(parent.context)
                .inflate(R.layout.favourite_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val favourite = favourites[position]
        holder.title.text = favourite.displayTitle.ifEmpty { favourite.headline }
        if (favourite.src != null)
            Glide.with(holder.itemView).load(favourite.src).into(holder.image)

        holder.title.setOnClickListener {
            if (holder.title.alpha == MAX_ALPHA) {
                holder.title.alpha = MIN_ALPHA
                holder.publishDate.alpha = MIN_ALPHA

            } else {
                holder.title.alpha = MAX_ALPHA
                holder.publishDate.alpha = MAX_ALPHA
            }

        }
        holder.removeIv.setOnClickListener { view->
            favourites.removeAt(position)
            clickListener.removeFavouriteClick(favourite, position)
            notifyItemRemoved(position)
            notifyDataSetChanged()
        }
        holder.description.text = favourite.description
        holder.publishDate.text = favourite.publicationDate
        holder.readBtn.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(favourite.url))
            startActivity(holder.itemView.context, browserIntent, null)
        }
    }

    override fun getItemCount() = favourites.size
    companion object{
        private const val MAX_ALPHA = 1.0F
        private const val MIN_ALPHA = 0.2F
    }
}