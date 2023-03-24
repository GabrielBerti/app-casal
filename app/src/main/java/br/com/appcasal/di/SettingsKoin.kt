package br.com.appcasal.di

import br.com.appcasal.BuildConfig
import br.com.appcasal.dao.datasource.remote.MetaRemoteDataSource
import br.com.appcasal.dao.dto.network.CorporativeRetrofit
import br.com.appcasal.dao.repository.MetaRepository
import br.com.appcasal.dao.service.MetaService
import br.com.appcasal.domain.usecase.*
import br.com.appcasal.viewmodel.MetaViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val settingsServiceModule = module {
    single<MetaService> {
        CorporativeRetrofit(get(), BuildConfig.BASE_URL).retrofit.create(
            MetaService::class.java
        )
    }
}

val settingsDataSourceModule = module {
    single {
        MetaRemoteDataSource(get())
    }
}

val settingsRepositoryModule = module {
    single { MetaRepository(get()) }
}

val settingsUseCaseModule = module {
    factory { GetMetasUseCase(get()) }
    factory { GetMetasByFilterUseCase(get()) }
    factory { InsereMetaUseCase(get()) }
    factory { AlteraMetaUseCase(get()) }
    factory { DeletaMetaUseCase(get()) }
}

val settingsViewModelModule = module {
    viewModel { MetaViewModel(get(), get(), get(), get(), get()) }
}

val settingsKoinModule = settingsServiceModule +
        settingsDataSourceModule +
        settingsUseCaseModule +
        settingsRepositoryModule +
        settingsViewModelModule
