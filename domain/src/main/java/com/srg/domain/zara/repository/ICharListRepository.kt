package com.srg.domain.zara.repository

import com.srg.domain.base.DataResult
import com.srg.domain.zara.entity.CharList

interface ICharListRepository {
    suspend fun getCharList(page: Int?): DataResult<CharList>
    suspend fun searchCharByName(page: Int?, query: String?): DataResult<CharList>
}