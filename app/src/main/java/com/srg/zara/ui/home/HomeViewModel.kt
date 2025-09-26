package com.srg.zara.ui.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.srg.zara.util.receiveAsEventFlow
import com.srg.domain.base.DataResult
import com.srg.domain.zara.entity.CharList
import com.srg.domain.zara.entity.Character
import com.srg.domain.zara.usecase.GetCharListUseCase
import com.srg.domain.zara.usecase.SearchCharUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCharListUseCase: GetCharListUseCase,
    private val searchCharUseCase: SearchCharUseCase,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _charsList = Channel<DataResult<CharList>>()
    val charList = _charsList.receiveAsEventFlow()

    fun getCharList(page: Int?, delayAmount: Int?) {
        viewModelScope.launch {
            if (delayAmount != null) {
                delay(delayAmount.toLong()) // Just to showcase shimmer while loading
            }
            val result = getCharListUseCase(page)
            _charsList.trySend(result)
        }
    }

    fun searchCharacters(page: Int?, query: String?) {
        viewModelScope.launch {
            val result = searchCharUseCase(page, query)
            _charsList.trySend(result)
        }
    }

    fun setStartingPage() {
        currentPage = 1
    }

    fun increasePage() {
        currentPage = currentPage?.plus(1)
    }

    fun clearCharacters() {
        characters = null
    }

    /**
     * We save the character list in memory so in case of the app process is killed no network call
     * is needed.
     **/
    var characters: ArrayList<Character>?
        set(value) {
            savedStateHandle["characters"] = value
        }
        get() = savedStateHandle["characters"]

    var currentPage: Int?
        set(value) {
            savedStateHandle["currentPage"] = value
        }
        get() = savedStateHandle["currentPage"]

    var maxPage: Int?
        set(value) {
            savedStateHandle["maxPage"] = value
        }
        get() = savedStateHandle["maxPage"]

}