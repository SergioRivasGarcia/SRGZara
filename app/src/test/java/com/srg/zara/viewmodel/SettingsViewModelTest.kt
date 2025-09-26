package com.srg.zara.viewmodel

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.srg.zara.ui.settings.SettingsViewModel
import com.srg.zara.util.CountryFlags
import com.srg.zara.util.LanguageManager
import kotlinx.coroutines.test.runTest
import okhttp3.Cache
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever

class SettingsViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var viewModel: SettingsViewModel

    @Mock
    lateinit var languageManager: LanguageManager

    @Mock
    lateinit var countryFlags: CountryFlags

    @Mock
    lateinit var cache: Cache

    @Mock
    lateinit var context: Context


    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        viewModel = SettingsViewModel(languageManager, countryFlags, cache)
    }

    @Test
    fun `getLanguage happy path is success`() = runTest {
        // given
        val language = "en"
        val mock = languageManager
        whenever(mock.getLanguage(context)).thenReturn(language)


        // when
        val result = viewModel.getLanguage(context)

        // then
        MatcherAssert.assertThat(language, CoreMatchers.equalTo(result))
    }

    @Test
    fun `getFlag happy path is success`() = runTest {
        // given
        val countryCode = "gb"
        val mock = countryFlags
        whenever(mock.getCountryFlagByCountryCode(countryCode)).thenReturn(" \uD83C\uDDEC\uD83C\uDDE7")

        // when
        val result = viewModel.getCountryFlagIcon(countryCode)

        // then
        MatcherAssert.assertThat(" \uD83C\uDDEC\uD83C\uDDE7", CoreMatchers.equalTo(result))
    }

}