package com.srg.data.datasource

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.srg.data.zara.datasource.CharsDataSource
import com.srg.data.zara.remote.CharsApi
import com.srg.domain.base.DataResult
import com.srg.domain.zara.entity.CharList
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.SocketPolicy
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.nio.charset.Charset

class CharsDataSourceTest {
    private lateinit var sut: CharsDataSource
    private lateinit var service: CharsApi
    private lateinit var server: MockWebServer
    private lateinit var gson: Gson

    @Before
    fun setUp() {
        server = MockWebServer()
        gson = GsonBuilder().create()
        service = Retrofit.Builder()
            .baseUrl(server.url(""))
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(CharsApi::class.java)
        sut = CharsDataSource(service)
    }

    private fun getFileContents(path: String): String {
        val stream = requireNotNull(this.javaClass.classLoader?.getResource(path))
        return stream.readBytes().toString(Charset.defaultCharset())
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun `getCharList happy path response is deserialized`() = runBlocking {
        // given
        val mockResponse = MockResponse()
        val body = getFileContents("get_char_list_response_happy.json")
        mockResponse.setBody(body)
        server.enqueue(mockResponse)

        // when
        val result = sut.getCharList(ArgumentMatchers.anyInt())

        // then
        MatcherAssert.assertThat(result, CoreMatchers.instanceOf(DataResult.Success::class.java))
        MatcherAssert.assertThat(
            (result as DataResult.Success).data,
            CoreMatchers.instanceOf(CharList::class.java)
        )
    }

    @Test
    fun `searchCharByName happy path response is deserialized`() = runBlocking {
        // given
        val mockResponse = MockResponse()
        val body = getFileContents("search_char_by_name_response_happy.json")
        mockResponse.setBody(body)
        server.enqueue(mockResponse)

        // when
        val result = sut.searchCharByName(ArgumentMatchers.anyInt(), ArgumentMatchers.anyString())

        // then
        MatcherAssert.assertThat(result, CoreMatchers.instanceOf(DataResult.Success::class.java))
        MatcherAssert.assertThat(
            (result as DataResult.Success).data,
            CoreMatchers.instanceOf(CharList::class.java)
        )
    }

    @Test
    fun `test error response returns DataResultError exception`() = runBlocking {
        // given
        val mockResponse = MockResponse()
        val body = getFileContents("error_response.json")
        mockResponse.setResponseCode(422)
        mockResponse.setBody(body)
        server.enqueue(mockResponse)

        // when
        val result = sut.searchCharByName(ArgumentMatchers.anyInt(), ArgumentMatchers.anyString())

        // then
        MatcherAssert.assertThat(result, CoreMatchers.instanceOf(DataResult.Error::class.java))
        MatcherAssert.assertThat(
            (result as DataResult.Error).exception.message,
            CoreMatchers.equalTo("There is nothing here")
        )
    }

    @Test
    fun `test network error returns DataResultError exception`() = runBlocking {
        // given
        val mockResponse = MockResponse()
        mockResponse.socketPolicy = SocketPolicy.DISCONNECT_AT_START
        server.enqueue(mockResponse)

        // when
        val result = sut.getCharList(ArgumentMatchers.anyInt())

        // then
        MatcherAssert.assertThat(result, CoreMatchers.instanceOf(DataResult.Error::class.java))
    }
}