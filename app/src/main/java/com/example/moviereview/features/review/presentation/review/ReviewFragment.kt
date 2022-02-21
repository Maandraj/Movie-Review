package com.example.moviereview.features.review.presentation.review

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.*
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.moviereview.BuildConfig
import com.example.moviereview.R
import com.example.moviereview.databinding.FragmentReviewBinding
import com.example.moviereview.features.review.domain.model.room.Favourites
import com.example.moviereview.features.review.presentation.interfaces.ClickAddListener
import com.example.moviereview.features.review.presentation.interfaces.ClickRemoveListener
import com.example.moviereview.features.review.presentation.review.adapter.ReviewAdapter
import com.example.moviereview.features.review.utils.SearchHelper
import com.example.moviereview.utils.checkNetwork
import com.example.moviereview.utils.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*


@AndroidEntryPoint
class ReviewFragment : Fragment(R.layout.fragment_review), ClickAddListener, ClickRemoveListener {
    private val viewModel: ReviewViewModel by viewModels()
    private var loadStateListener: Unit? = null
    private var query = ""
    private val adapter by lazy(LazyThreadSafetyMode.NONE) {
        ReviewAdapter(this, this)
    }
    private val binding: FragmentReviewBinding by viewBinding(FragmentReviewBinding::bind)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        stateSearch(viewModel.searching.value ?: false)
        getReviews()
        binding.viewSearch.tvResultsNumber.text =
            getString(R.string.results_num, "20")


        (binding.rvReview.itemAnimator as SimpleItemAnimator).changeDuration = 0
        viewModel.loading.observe(viewLifecycleOwner) {
            binding.mainProgress.root.visibility = if (it) View.VISIBLE else View.GONE
        }
        viewModel.exception.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(),
                getString(R.string.error_text_toast) + it.message,
                Toast.LENGTH_SHORT).show()
        }
        activateListeners()

    }

    private fun getReviews() {
        lifecycleScope.launch {
            binding.mainProgress.root.visibility = View.VISIBLE
            viewModel.getReviews(query)
                .observe(viewLifecycleOwner) {
                    if (binding.rvReview.adapter == null) {
                        binding.rvReview.layoutManager = LinearLayoutManager(requireContext())
                        binding.rvReview.adapter = adapter
                        val snapHelper = PagerSnapHelper()
                        snapHelper.attachToRecyclerView(binding.rvReview)
                    }
                    adapter.submitData(lifecycle, it)
                    this.cancel()
                    return@observe

                }

        }
    }

    private fun activateListeners() {
        binding.ivSearch.setOnClickListener {
            stateSearch(!binding.viewSearch.cvSearch.isVisible)
        }
        binding.viewSearch.etSearchBox.doAfterTextChanged { text -> query = text?.toString() ?: "" }

        binding.viewSearch.tiSearchLayout.setStartIconOnClickListener {
            stateSearch(!binding.viewSearch.cvSearch.isVisible)
        }
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    binding.rvReview.scrollToPosition(0)
                    super.isEnabled()
                }
            })
        loadStateListener = adapter.addLoadStateListener { state ->
            val stateRefresh = state.refresh
            val itemCount = adapter.itemCount

            if (BuildConfig.DEBUG) {
                Log.i("stateRefresh", stateRefresh.toString())
                Log.i("statePrepend", state.prepend.toString())
                Log.i("stateAppend", state.append.toString())
            }
            try {
                if (adapter.itemCount < 1) {
                    binding.viewSearch.tvResultsNumber.text = getString(R.string.results_num, "0")
                } else {
                    binding.tvNotFound.visibility = View.GONE
                    binding.viewSearch.tvResultsNumber.text =
                        getString(R.string.results_num,
                            "$itemCount (${if (itemCount >= 20) (itemCount / 20) else 1})")
                }

                lifecycleScope.launch {
                if (state.append is LoadState.Error || stateRefresh is LoadState.Error) {
                    binding.refreshLayout.isRefreshing = false
                    binding.mainProgress.root.visibility = View.GONE

                    if (!requireContext().checkNetwork()) {
                        if (itemCount < 1)
                            binding.mainProgress.root.visibility = View.VISIBLE
                        else
                            binding.listProgress.visibility = View.VISIBLE

                        Toast.makeText(requireContext(),
                            getString(R.string.error__update_network),
                            Toast.LENGTH_LONG).show()

                            while (!requireContext().checkNetwork()) {
                                if (BuildConfig.DEBUG)
                                    Log.i("state", "Пытаюсь получить доступ в интернет")
                                delay(3000)
                            }
                            adapter.retry()

                    }else if (stateRefresh is LoadState.Error){
                        binding.tvNotFound.visibility = View.VISIBLE
                        Toast.makeText(requireContext(),
                            getString(R.string.error_search),
                            Toast.LENGTH_LONG).show()
                    }else if (state.append is LoadState.Error){   Toast.makeText(requireContext(),
                        getString(R.string.append_end_text),
                        Toast.LENGTH_LONG).show()}
                    else{
                        Toast.makeText(requireContext(),
                            getString(R.string.error),
                            Toast.LENGTH_LONG).show()
                    }

                }
                if (state.append is LoadState.Loading ){
                    binding.listProgress.visibility = View.VISIBLE
                }
                if (stateRefresh is LoadState.Loading) {
                    binding.tvNotFound.visibility = View.GONE
                    binding.mainProgress.root.visibility = View.VISIBLE
                }

                if (stateRefresh is LoadState.NotLoading) {
                    binding.refreshLayout.isRefreshing = false
                    binding.listProgress.visibility = View.GONE
                    binding.mainProgress.root.visibility = View.GONE
                } }
            } catch (ex: IllegalStateException) {
                Log.e(TAG, "IllegalStateException ${ex.message}")
            }
        }
        binding.viewSearch.tvRemove.setOnClickListener {
            stateSearch(false)
            binding.viewSearch.etSearchBox.setText("")
            binding.listProgress.visibility = View.GONE
            binding.rvReview.scrollToPosition(0)
            getReviews()
        }
        binding.viewSearch.tiSearchLayout.setEndIconOnClickListener {
            stateSearch(false)
            binding.listProgress.visibility = View.GONE
            binding.rvReview.scrollToPosition(0)
            getReviews()
        }
        binding.refreshLayout.setOnRefreshListener {
            getReviews()
        }
    }

    override fun onDestroyView() {
        adapter.removeLoadStateListener { loadStateListener }
        super.onDestroyView()

    }

    private fun stateSearch(show: Boolean) {
        viewModel.setStateSearch(show)
        SearchHelper.stateSearch(show,
            target = binding.viewSearch.cvSearch,
            searchImage = binding.ivSearch,
            root = binding.root)
        if (!show)
            hideKeyboard()

    }


    override fun addFavouriteClick(favourites: Favourites, position: Int) {
        viewModel.saveFavourites(favourites)

    }

    override fun removeFavouriteClick(favourites: Favourites, position: Int) {
        viewModel.removeFavourites(favourites)
    }


    companion object {
        private const val TAG: String = "ReviewFragment"
    }
}