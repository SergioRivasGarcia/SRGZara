package com.srg.domain.zara.usecase

import com.srg.domain.zara.repository.ICharListRepository
import javax.inject.Inject

class GetCharListUseCase @Inject constructor(private val charsListRepository: ICharListRepository) {
    suspend operator fun invoke(page: Int?) = charsListRepository.getCharList(page)
}