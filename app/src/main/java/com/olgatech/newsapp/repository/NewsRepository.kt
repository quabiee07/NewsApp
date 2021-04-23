package com.olgatech.newsapp.repository

import com.olgatech.newsapp.api.RetrofitInstance
import com.olgatech.newsapp.db.ArticleDatabase
import com.olgatech.newsapp.models.Article
import retrofit2.Retrofit

class NewsRepository(
        val db : ArticleDatabase
) {
    suspend fun getBreakingNews(countryCode: String, pageNumber:Int)=
            RetrofitInstance.api.getBreakingNews(countryCode, pageNumber)

    suspend fun searchNews(searchQuery: String, pageNumber: Int) =
            RetrofitInstance.api.searchForNews(searchQuery, pageNumber)

    suspend fun upsert(article:Article) = db.getArticleDao().upsert(article)

    fun getSavedNews() = db.getArticleDao().getAllArticles()

    suspend fun deleteArticle(article: Article) = db.getArticleDao().deleteArticle(article)
}