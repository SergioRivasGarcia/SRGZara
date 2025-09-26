package com.srg.zara.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.srg.domain.zara.entity.Character
import com.srg.domain.zara.entity.Location
import com.srg.domain.zara.entity.Origin
import com.srg.zara.ui.detail.CharDetailViewModel
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever

class CharDetailViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var viewModel: CharDetailViewModel

    @Mock
    lateinit var savedStateHandle: SavedStateHandle

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        viewModel = CharDetailViewModel(savedStateHandle)
    }

    @Test
    fun `character returns a Character object`() = runTest {
        // given
        val mock = Character(
            id = 123,
            name = "TestName",
            status = "Alive",
            species = "Human",
            type = "",
            gender = "Unknown",
            origin = Origin(
                name = "Earth",
                url = "www.sample.com"
            ),
            location = Location(
                name = "Earth",
                url = "www.sample.com"
            ),
            image = "www.sample.com/1.png",
            episode = listOf("www.sample.com/1", "www.sample.com/2", "www.sample.com/3"),
            url = "www.sample.com",
            created = "2017-11-04T18:48:46.250Z"
        )
        whenever(viewModel.character).thenReturn(mock)

        // when
        val result = viewModel.character

        // then
        MatcherAssert.assertThat(result?.name, CoreMatchers.equalTo("TestName"))
        MatcherAssert.assertThat(result?.id, CoreMatchers.instanceOf(Long::class.java))
        MatcherAssert.assertThat(result?.episode, CoreMatchers.hasItems<String>("www.sample.com/1"))
        MatcherAssert.assertThat(result?.episode?.size, CoreMatchers.equalTo(3))
    }

}