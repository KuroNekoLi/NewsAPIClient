package com.anushka.newsapiclient.domain.repository

import androidx.lifecycle.LiveData
import com.anushka.newsapiclient.data.model.APIResponse
import com.anushka.newsapiclient.data.model.Article
import com.anushka.newsapiclient.data.util.Resource
import kotlinx.coroutines.flow.Flow

interface NewsRepository{

        suspend fun getNewsHeadlines(country:String,page:Int):Resource<APIResponse>
        suspend fun getSearchedNews(searchQuery:String):Resource<APIResponse>
        suspend fun saveNews(article: Article)
        suspend fun deleteNews(article: Article)
        fun getSaveNews(): Flow<List<Article>>
}