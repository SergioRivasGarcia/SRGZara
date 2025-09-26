package com.srg.zara.viewmodel


import com.srg.domain.base.DataResult
import com.srg.domain.base.DataResult.Error
import com.srg.domain.base.DataResult.Status
import com.srg.domain.base.DataResult.Success

import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Test

class ExtensionsTest {
    @Test
    fun `DataResult returns Success for DataResult_Success`() {
        // Given
        val dataString = "Test"
        val dataResult = Success(dataString)

        // When
        val result = dataResult

        // Then
        MatcherAssert.assertThat(result, CoreMatchers.instanceOf(Success::class.java))
        MatcherAssert.assertThat(result.data, CoreMatchers.equalTo(dataString))
    }

    @Test
    fun `DataResult returns Error for DataResult_Error`() {
        // Given
        val ex = Exception("Test")
        val dataResult = Error(ex)

        // When
        val result = dataResult

        // Then
        MatcherAssert.assertThat(result, CoreMatchers.instanceOf(Error::class.java))
        MatcherAssert.assertThat((result as Error).exception, CoreMatchers.equalTo(ex))
    }

    @Test
    fun `DataResult returns Loading for DataResult_DataStatus_LOADING`() {
        // Given
        val status = DataResult.DataStatus.LOADING
        val dataResult = Status(status)

        // When
        val result = dataResult

        // Then
        MatcherAssert.assertThat(result, CoreMatchers.instanceOf(Status::class.java))
    }

}
