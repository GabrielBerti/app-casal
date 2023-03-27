package br.com.appcasal.di

import br.com.appcasal.BuildConfig
import br.com.appcasal.dao.datasource.remote.IngredienteRemoteDataSource
import br.com.appcasal.dao.datasource.remote.MetaRemoteDataSource
import br.com.appcasal.dao.datasource.remote.ReceitaRemoteDataSource
import br.com.appcasal.dao.datasource.remote.TransacaoRemoteDataSource
import br.com.appcasal.dao.dto.network.CorporativeRetrofit
import br.com.appcasal.dao.repository.IngredienteRepository
import br.com.appcasal.dao.repository.MetaRepository
import br.com.appcasal.dao.repository.ReceitaRepository
import br.com.appcasal.dao.repository.TransacaoRepository
import br.com.appcasal.dao.service.IngredienteService
import br.com.appcasal.dao.service.MetaService
import br.com.appcasal.dao.service.ReceitaService
import br.com.appcasal.dao.service.TransacaoService
import br.com.appcasal.domain.usecase.*
import br.com.appcasal.viewmodel.IngredienteViewModel
import br.com.appcasal.viewmodel.MetaViewModel
import br.com.appcasal.viewmodel.ReceitaViewModel
import br.com.appcasal.viewmodel.TransacaoViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val settingsServiceModule = module {
    single<MetaService> {
        CorporativeRetrofit(get(), BuildConfig.BASE_URL).retrofit.create(
            MetaService::class.java
        )
    }

    single<TransacaoService> {
        CorporativeRetrofit(get(), BuildConfig.BASE_URL).retrofit.create(
            TransacaoService::class.java
        )
    }

    single<ReceitaService> {
        CorporativeRetrofit(get(), BuildConfig.BASE_URL).retrofit.create(
            ReceitaService::class.java
        )
    }

    single<IngredienteService> {
        CorporativeRetrofit(get(), BuildConfig.BASE_URL).retrofit.create(
            IngredienteService::class.java
        )
    }
}

val settingsDataSourceModule = module {
    single { MetaRemoteDataSource(get()) }
    single { TransacaoRemoteDataSource(get()) }
    single { ReceitaRemoteDataSource(get()) }
    single { IngredienteRemoteDataSource(get()) }
}

val settingsRepositoryModule = module {
    single { MetaRepository(get()) }
    single { TransacaoRepository(get()) }
    single { ReceitaRepository(get()) }
    single { IngredienteRepository(get()) }
}

val settingsUseCaseModule = module {
    factory { GetMetasUseCase(get()) }
    factory { GetMetasByFilterUseCase(get()) }
    factory { InsereMetaUseCase(get()) }
    factory { AlteraMetaUseCase(get()) }
    factory { DeletaMetaUseCase(get()) }

    factory { GetTransacoesUseCase(get()) }
    factory { InsereTransacaoUseCase(get()) }
    factory { AlteraTransacaoUseCase(get()) }
    factory { DeletaTransacaoUseCase(get()) }
    factory { DeletaTodasTransacoesUseCase(get()) }

    factory { GetReceitasUseCase(get()) }
    factory { InsereReceitaUseCase(get()) }
    factory { AlteraReceitaUseCase(get()) }
    factory { DeletaReceitaUseCase(get()) }

    factory { InsereIngredienteUseCase(get()) }
    factory { AlteraIngredienteUseCase(get()) }
    factory { DeletaIngredienteUseCase(get()) }
    factory { RecuperaIngredienteByReceitaUseCase(get()) }
    factory { MarcarDesmarcarIngredienteUseCase(get()) }
    factory { DesmarcarTodosIngredientesUseCase(get()) }
}

val settingsViewModelModule = module {
    viewModel { MetaViewModel(get(), get(), get(), get(), get()) }
    viewModel { TransacaoViewModel(get(), get(), get(), get(), get()) }
    viewModel { ReceitaViewModel(get(), get(), get(), get()) }
    viewModel { IngredienteViewModel(get(), get(), get(), get(), get(), get()) }
}

val settingsKoinModule = settingsServiceModule +
        settingsDataSourceModule +
        settingsUseCaseModule +
        settingsRepositoryModule +
        settingsViewModelModule
