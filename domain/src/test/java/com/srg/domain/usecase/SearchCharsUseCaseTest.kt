package com.srg.domain.usecase


import com.srg.domain.base.DataResult
import com.srg.domain.base.DataResult.Success
import com.srg.domain.zara.entity.CharList
import com.srg.domain.zara.entity.Character
import com.srg.domain.zara.entity.Location
import com.srg.domain.zara.entity.Origin
import com.srg.domain.zara.repository.ICharListRepository
import com.srg.domain.zara.usecase.SearchCharUseCase
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class SearchCharsUseCaseTest {

    lateinit var sut: SearchCharUseCase
    private lateinit var repo: ICharListRepository


    @Test
    fun `characters are returned on successful call`() = runBlocking {
        // given
        val charList: CharList = mock()
        val char: Character = mock()

        whenever(charList.characters).thenReturn(listOf(char, char, char))

        repo = mock {
            onBlocking { searchCharByName(1, "TestName") } doReturn runBlocking {
                Success(charList)
            }
        }

        // when
        sut = SearchCharUseCase(repo)
        val result = sut(1, "TestName")

        // then
        MatcherAssert.assertThat(result, instanceOf(Success::class.java))
        MatcherAssert.assertThat(
            (result as Success<CharList>).data?.characters,
            CoreMatchers.hasItems(char)
        )
        MatcherAssert.assertThat(result.data?.characters?.size, CoreMatchers.equalTo(3))
    }

    @Test
    fun `characters are returned on successful call containing specific character`() = runBlocking {
        // given
        val charList: CharList = mock()
        val char: Character = mock()
        val specificChar = Character(
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

        whenever(charList.characters).thenReturn(listOf(char, char, char, specificChar))

        repo = mock {
            onBlocking { searchCharByName(1, "TestName") } doReturn runBlocking {
                Success(charList)
            }
        }

        // when
        sut = SearchCharUseCase(repo)
        val result = sut(1, "TestName")

        // then
        MatcherAssert.assertThat(
            (result as Success<CharList>).data?.characters,
            CoreMatchers.hasItem(specificChar)
        )
    }

    @Test
    fun `error is returned when SearchCharUseCase fails`() = runBlocking {
        // given
        repo = mock {
            onBlocking { searchCharByName(1, "TestName") } doReturn runBlocking {
                DataResult.Error(Exception("Failed to retrieve characters."))
            }
        }

        // when
        sut = SearchCharUseCase(repo)
        val result = sut(1, "TestName")

        // then
        MatcherAssert.assertThat(result, instanceOf(DataResult.Error::class.java))
        MatcherAssert.assertThat(
            (result as DataResult.Error).exception.message,
            CoreMatchers.equalTo("Failed to retrieve characters.")
        )
    }
}