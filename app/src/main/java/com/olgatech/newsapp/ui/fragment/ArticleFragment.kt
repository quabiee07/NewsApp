package com.olgatech.newsapp.ui.fragment

import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.olgatech.newsapp.NewsActivity
import com.olgatech.newsapp.R
import com.olgatech.newsapp.ui.NewsViewModel
import kotlinx.android.synthetic.main.fragment_article.*

class ArticleFragment : Fragment(R.layout.fragment_article) {
    lateinit var viewModel: NewsViewModel
    val args: ArticleFragmentArgs by  navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel
        val article= args.article
        webView.apply {
            webViewClient = WebViewClient()
            loadUrl(article.url)
            addJavascriptInterface(webView,"Article")
        }

        fab.setOnClickListener {
            viewModel.saveArticle(article)
            Snackbar.make(view, " Article saved Successfully", Snackbar.LENGTH_SHORT).show()

        }
    }

}