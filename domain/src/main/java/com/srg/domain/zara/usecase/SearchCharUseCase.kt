package com.srg.domain.zara.usecase

import com.srg.domain.zara.repository.ICharListRepository
import javax.inject.Inject

class SearchCharUseCase @Inject constructor(private val charListRepository: ICharListRepository) {
    suspend operator fun invoke(page: Int?, query: String?) =
        charListRepository.searchCharByName(page, query)
}