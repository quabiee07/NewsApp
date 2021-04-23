package com.olgatech.newsapp.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AbsListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.olgatech.newsapp.NewsActivity
import com.olgatech.newsapp.R
import com.olgatech.newsapp.adapters.NewsAdapter
import com.olgatech.newsapp.ui.NewsViewModel
import com.olgatech.newsapp.util.Constants.Companion.QUERY_PAGE_SIZE
import com.olgatech.newsapp.util.Resource
import kotlinx.android.synthetic.main.fragment_beaking_news.*


class BreakingNewsFragment : Fragment(R.layout.fragment_beaking_news) {

    lateinit var viewModel: NewsViewModel
    lateinit var  newsAdapter: NewsAdapter

    val TAG = "Breaking News Fragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel
        setupRecyclerView()

        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply{
                putSerializable("article", it)
            }
            findNavController().navigate(
                    R.id.action_breakingNewsFragment_to_articleFragment,
                    bundle)
        }

        viewModel.breakingNews.observe(viewLifecycleOwner, Observer { response->
            when(response){
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles.toList())
                        val totalPages = newsResponse.totalResults / QUERY_PAGE_SIZE + 2
                        isLastPage = viewModel.breakingNewsPage == totalPages
                        if(isLastPage) {
                            rv_breaking_news.setPadding(0,0,0,0)

                        }
                    }
                }
                is Resource.Error->{
                    hideProgressBar()
                    response.message?.let { message->
                        Log.e(TAG, "An error occurred: $message")
                    }
                }
                is Resource.Loading->{
                    showProgressBar()
                }
            }

        })
    }

    private fun hideProgressBar() {
        paginationProgressBar.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressBar() {
        paginationProgressBar.visibility = View.VISIBLE
        isLoading =true
    }

    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    val scrollListener= object : RecyclerView.OnScrollListener(){
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
                isScrolling = true
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning &&
                    isTotalMoreThanVisible && isScrolling
            if (shouldPaginate) {
                viewModel.getBreakingNews("us")
                isScrolling = false
            }
          }

        }

    private fun setupRecyclerView(){
        newsAdapter = NewsAdapter()
        rv_breaking_news.apply {
            adapter = newsAdapter
            newsAdapter.notifyDataSetChanged()
            addOnScrollListener(this@BreakingNewsFragment.scrollListener)
        }
    }
    }




