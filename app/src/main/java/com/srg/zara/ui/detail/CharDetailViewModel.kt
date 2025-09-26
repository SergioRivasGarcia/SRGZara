package com.srg.zara.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.srg.domain.zara.entity.Character
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CharDetailViewModel @Inject constructor(private val savedStateHandle: SavedStateHandle) :
    ViewModel() {

    /**
     * We save the character in memory so in case of the app process is killed no network call is
     * needed.
     **/
    var character: Character?
        set(value) {
            savedStateHandle["character"] = value
        }
        get() = savedStateHandle["character"]

    fun resetData() {
        character = null
    }
}