package com.anushka.newsapiclient.data.api

import com.google.common.truth.Truth
import kotlinx.coroutines.runBlocking
import mockwebserver3.MockResponse
import mockwebserver3.MockWebServer
import okio.buffer
import okio.source
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class NewsAPIServiceTest {
    private lateinit var service:NewsAPIService
    private lateinit var server:MockWebServer

    @Before
    fun setUp() {
        server = MockWebServer()
        service = Retrofit.Builder()
            .baseUrl(server.url(""))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NewsAPIService::class.java)
    }

    private fun enqueueMockResponse(
        fileName:String
    ){
        val inputStream = javaClass.classLoader!!.getResourceAsStream(fileName)
        val source = inputStream.source().buffer()
        val mockResponse = MockResponse()
        mockResponse.setBody(source.readString(Charsets.UTF_8))
        server.enqueue(mockResponse)
    }

    @Test
    fun getTopHeadlines_sentRequest_receivedExpected(){
        runBlocking {
            enqueueMockResponse("newsresponse.json")
            val responseBody = service.getTopHeadlines("us",1).body()
            val request = server.takeRequest()
            Truth.assertThat(responseBody).isNotNull()
            Truth.assertThat(request.path).isEqualTo("/v2/top-headlines?country=us&page=1&apiKey=e2960cc78cdf4505be6a0100ddbf4579")
        }
    }

    @Test
    fun getTopHeadlines_receivedResponse_correctPageSize(){
        runBlocking {
            enqueueMockResponse("newsresponse.json")
            val responseBody = service.getTopHeadlines("us",1).body()
            val artistsList = responseBody!!.articles
            Truth.assertThat(artistsList.size).isEqualTo(20)
        }
    }

    @Test
    fun getTopHeadlines_receivedResponse_correctContent(){
        runBlocking {
            enqueueMockResponse("newsresponse.json")
            val responseBody = service.getTopHeadlines("us",1).body()
            val artistsList = responseBody!!.articles
            val article = artistsList[0]
            Truth.assertThat(article.author).isEqualTo("Tomasz Grotnik")
            Truth.assertThat(article.url).isEqualTo( "https://www.navalnews.com/naval-news/2023/06/polish-coastal-defence-system-with-nsm-missiles-for-ukraine/")
            Truth.assertThat(article.publishedAt).isEqualTo("2023-06-30T08:33:53Z")
        }
    }

    @After
    fun tearDown() {
        server.shutdown()
    }
}