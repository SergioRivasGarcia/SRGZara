package com.srg.domain.usecase


import com.srg.domain.base.DataResult
import com.srg.domain.base.DataResult.Success
import com.srg.domain.zara.entity.CharList
import com.srg.domain.zara.entity.Character
import com.srg.domain.zara.repository.ICharListRepository
import com.srg.domain.zara.usecase.GetCharListUseCase
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class GetCharsUseCaseTest {

    lateinit var sut: GetCharListUseCase
    private lateinit var repo: ICharListRepository


    @Test
    fun `characters are returned on successful call`() = runBlocking {
        // given
        val charList: CharList = mock()
        val char: Character = mock()

        whenever(charList.characters).thenReturn(listOf(char, char, char, char))

        repo = mock {
            onBlocking { getCharList(6) } doReturn runBlocking {
                Success(charList)
            }
        }

        // when
        sut = GetCharListUseCase(repo)
        val result = sut(6)

        // then
        MatcherAssert.assertThat(result, instanceOf(Success::class.java))
        MatcherAssert.assertThat(
            (result as Success<CharList>).data?.characters,
            CoreMatchers.hasItems(char)
        )
        MatcherAssert.assertThat(result.data?.characters?.size, CoreMatchers.equalTo(4))
    }

    @Test
    fun `error is returned when GetCharsListUseCase fails`() = runBlocking {
        // given
        repo = mock {
            onBlocking { getCharList(6) } doReturn runBlocking {
                DataResult.Error(Exception("Failed to retrieve characters."))
            }
        }

        // when
        sut = GetCharListUseCase(repo)
        val result = sut(6)

        // then
        MatcherAssert.assertThat(result, instanceOf(DataResult.Error::class.java))
        MatcherAssert.assertThat(
            (result as DataResult.Error).exception.message,
            CoreMatchers.equalTo("Failed to retrieve characters.")
        )
    }
}