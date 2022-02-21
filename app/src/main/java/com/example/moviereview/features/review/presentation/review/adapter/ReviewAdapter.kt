package com.example.moviereview.features.review.presentation.review.adapter

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.example.moviereview.R
import com.example.moviereview.databinding.ReviewItemBinding
import com.example.moviereview.features.review.domain.model.Result
import com.example.moviereview.features.review.domain.model.mapper.FavouriteMapper
import com.example.moviereview.features.review.presentation.interfaces.ClickAddListener
import com.example.moviereview.features.review.presentation.interfaces.ClickRemoveListener


class ReviewAdapter(
    private val clickAddListener: ClickAddListener,
    private val clickRemoveListener: ClickRemoveListener,
) :
    PagingDataAdapter<Result, ReviewAdapter.ViewHolder>(ResultDiffItemCallback) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.review_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    companion object {
        private const val MAX_ALPHA = 1.0F
        private const val MIN_ALPHA = 0.2F
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding: ReviewItemBinding by viewBinding(ReviewItemBinding::bind)
        fun bind(result: Result?) {
            with(binding) {

                Log.i("ReviewAdapter", "Pos $bindingAdapterPosition")
                if (result != null) {
                    tbAddRemove.isChecked = result.favourite
                    tvTitle.text = result.displayTitle.ifEmpty { result.headline }
                    if (result.src != null)
                        Glide.with(itemView).load(result.src).into(ivImage)
                    tvTitle.setOnClickListener {
                        if (tvTitle.alpha == MAX_ALPHA) {
                            tvTitle.alpha = MIN_ALPHA
                            tvPublishDate.alpha = MIN_ALPHA

                        } else {
                            tvTitle.alpha = MAX_ALPHA
                            tvPublishDate.alpha = MAX_ALPHA
                        }

                    }


                    tbAddRemove.setOnClickListener { view ->
                        val favourites =  FavouriteMapper().map(result)

                        if (!result.favourite) {
                            clickAddListener.addFavouriteClick(favourites,
                                absoluteAdapterPosition)
                            result.favourite = true
                        } else {
                            clickRemoveListener.removeFavouriteClick(favourites,
                                absoluteAdapterPosition)
                            result.favourite = false

                        }

                    }
                    tvDescription.text = result.description
                    tvPublishDate.text = result.publicationDate
                    btnRead.setOnClickListener {
                        if (result.url != null) {
                            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(result.url))
                            startActivity(itemView.context, browserIntent, null)
                        }
                    }
                }
            }
        }
    }

    private object ResultDiffItemCallback : DiffUtil.ItemCallback<Result>() {

        override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem.displayTitle == newItem.displayTitle && oldItem.url == newItem.url
        }
    }
}