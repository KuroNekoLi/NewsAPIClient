package com.anushka.newsapiclient.data.repository.dataSourceImpl

import com.anushka.newsapiclient.data.api.NewsAPIService
import com.anushka.newsapiclient.data.model.APIResponse
import com.anushka.newsapiclient.data.repository.dataSource.NewsRemoteDataSource
import retrofit2.Response

class NewsRemoteDataSourceImpl(
    private val newsAPIService: NewsAPIService,
):NewsRemoteDataSource {
    override suspend fun getTopHeadlines(country:String,page:Int): Response<APIResponse> {
        return newsAPIService.getTopHeadlines(country, page)
    }
}