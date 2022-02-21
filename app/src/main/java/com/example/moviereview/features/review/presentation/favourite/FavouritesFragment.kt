package com.example.moviereview.features.review.presentation.favourite

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.*
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.moviereview.R
import com.example.moviereview.databinding.FragmentReviewBinding
import com.example.moviereview.features.review.domain.model.room.Favourites
import com.example.moviereview.features.review.presentation.favourite.adapter.FavouriteAdapter
import com.example.moviereview.features.review.presentation.interfaces.ClickRemoveListener
import com.example.moviereview.features.review.utils.SearchHelper
import com.example.moviereview.utils.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FavouritesFragment : Fragment(R.layout.fragment_review), ClickRemoveListener{
    private val viewModel: FavouritesViewModel by viewModels()
    private val favourites = mutableListOf<Favourites>()
    private val binding: FragmentReviewBinding by viewBinding(FragmentReviewBinding::bind)
    private var query = ""
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
            viewModel.getFavourites(query)


        openSearch(viewModel.searching.value ?: false)
        viewModel.favourites.observe(viewLifecycleOwner) { results ->
            favourites.clear()
            binding.refreshLayout.isRefreshing = false

            if (!results.isNullOrEmpty()){
                binding.viewSearch.tvResultsNumber.text = getString(R.string.results_num, results.size.toString() )
                binding.tvNotFound.visibility = View.GONE
                favourites.addAll(results)}
            else{
                binding.viewSearch.tvResultsNumber.text = getString(R.string.results_num, "0" )
                binding.tvNotFound.visibility = View.VISIBLE
            }
            if (binding.rvReview.adapter == null) {


                binding.rvReview.layoutManager = LinearLayoutManager(requireContext())
                binding.rvReview.adapter = FavouriteAdapter(  favourites, this)
                val snapHelper = PagerSnapHelper()
                snapHelper.attachToRecyclerView(binding.rvReview)
            }
            binding.rvReview.adapter?.notifyDataSetChanged()
        }
        viewModel.loading.observe(viewLifecycleOwner) {
            binding.mainProgress.root.visibility = if (it) View.VISIBLE else View.GONE
        }
        viewModel.exception.observe(viewLifecycleOwner){
            Toast.makeText(requireContext(), getString(R.string.error_text_toast) + it.message, Toast.LENGTH_SHORT).show()
        }
        binding.refreshLayout.setOnRefreshListener {
            viewModel.getFavourites(query)
        }
        activateListeners()

    }


    private fun activateListeners(){
        binding.ivSearch.setOnClickListener {
            openSearch(!binding.viewSearch.cvSearch.isVisible)
        }
        binding.viewSearch.etSearchBox.doAfterTextChanged {text-> query = text?.toString() ?: "" }
        binding.viewSearch.tiSearchLayout.setStartIconOnClickListener {
            openSearch(false)
        }
        binding.viewSearch.tvRemove.setOnClickListener {
            openSearch(false)
            binding.rvReview.scrollToPosition(0)
            binding.viewSearch.etSearchBox.setText("")
            viewModel.getFavourites()
        }
        binding.viewSearch.tiSearchLayout.setEndIconOnClickListener {
            binding.rvReview.scrollToPosition(0)
            viewModel.getFavourites(query)
            openSearch(false)
        }
    }


    private fun openSearch(show: Boolean) {
        viewModel.setStateSearch(show)
        SearchHelper.stateSearch(show, target = binding.viewSearch.cvSearch, searchImage = binding.ivSearch, root = binding.root)
        if (!show)
            hideKeyboard()
    }



    override fun removeFavouriteClick(favourites: Favourites, position :Int) {
      viewModel.removeFavourites(favourites)
    }
}