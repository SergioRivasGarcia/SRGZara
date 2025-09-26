package com.srg.zara.di

import com.srg.data.zara.remote.CharsApi
import com.srg.data.zara.datasource.CharsDataSource
import com.srg.data.zara.repository.CharListRepository
import com.srg.domain.zara.repository.ICharListRepository
import com.srg.zara.util.CountryFlags
import com.srg.zara.util.LanguageManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {

    @Volatile
    var charsListRepository: CharListRepository? = null

    @Provides
    fun provideCharsListRepository(charsApi: CharsApi): ICharListRepository {
        synchronized(this) {
            return charsListRepository ?: createCharsListRepository(charsApi)
        }
    }

    private fun createCharsListRepository(charsApi: CharsApi): ICharListRepository {
        val newRepo =
            CharListRepository(
                CharsDataSource(charsApi)
            )
        charsListRepository = newRepo
        return newRepo
    }

    @Provides
    fun provideLanguageManager(): LanguageManager {
        return LanguageManager
    }

    @Provides
    fun provideCountryFlags(): CountryFlags {
        return CountryFlags
    }

}