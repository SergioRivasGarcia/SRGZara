package com.srg.data.zara.datasource

import com.srg.domain.base.DataResult
import com.srg.domain.zara.entity.CharList


interface ICharsRemoteDataSource {

    suspend fun getCharList(page: Int?): DataResult<CharList>

    suspend fun searchCharByName(page: Int?, query: String?): DataResult<CharList>

}