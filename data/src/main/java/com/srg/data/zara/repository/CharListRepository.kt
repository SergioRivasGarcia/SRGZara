package com.srg.data.zara.repository

import com.srg.data.zara.datasource.ICharsRemoteDataSource
import com.srg.domain.base.DataResult
import com.srg.domain.zara.entity.CharList
import com.srg.domain.zara.repository.ICharListRepository


class CharListRepository(private val charsRemoteDataSource: ICharsRemoteDataSource) :
    ICharListRepository {
    override suspend fun getCharList(page: Int?): DataResult<CharList> {
        return charsRemoteDataSource.getCharList(page)
    }

    override suspend fun searchCharByName(page: Int?, query: String?): DataResult<CharList> {
        return charsRemoteDataSource.searchCharByName(page, query)
    }
}