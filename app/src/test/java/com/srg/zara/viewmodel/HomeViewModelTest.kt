package com.srg.zara.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.srg.domain.base.DataResult
import com.srg.domain.zara.entity.CharList
import com.srg.domain.zara.usecase.GetCharListUseCase
import com.srg.domain.zara.usecase.SearchCharUseCase
import com.srg.zara.ui.home.HomeViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class HomeViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var getCharListUseCase: GetCharListUseCase

    @Mock
    lateinit var searchCharListUseCase: SearchCharUseCase


    private lateinit var viewModel: HomeViewModel

    @OptIn(DelicateCoroutinesApi::class)
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @Mock
    lateinit var savedStateHandle: SavedStateHandle

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        viewModel = HomeViewModel(
            getCharListUseCase, searchCharListUseCase, savedStateHandle
        )

        Dispatchers.setMain(mainThreadSurrogate)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset the main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }

    @Test
    fun `getCharListUseCase happy path is success`() = runTest {
        // given
        val charList: CharList = mock()
        whenever(getCharListUseCase(6)).thenReturn(
            DataResult.Success(
                charList
            )
        )

        viewModel.charList.flow.test {
            // when
            viewModel.getCharList(6, null)

            // then
            val item = awaitItem()
            MatcherAssert.assertThat(item, CoreMatchers.instanceOf(DataResult.Success::class.java))
            MatcherAssert.assertThat(
                (item as DataResult.Success).data,
                CoreMatchers.equalTo(charList)
            )
        }
    }

    @Test
    fun `getCharListUseCase is Error`() = runTest {
        // given
        whenever(getCharListUseCase(6)).thenReturn(
            DataResult.Error(
                Exception("Test")
            )
        )

        viewModel.charList.flow.test {
            // when
            viewModel.getCharList(6, null)

            // then
            val item = awaitItem()
            MatcherAssert.assertThat(item, CoreMatchers.instanceOf(DataResult.Error::class.java))
            MatcherAssert.assertThat(
                (item as DataResult.Error).exception.message,
                CoreMatchers.equalTo("Test")
            )
        }
    }
}