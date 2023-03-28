package br.com.appcasal.di

import br.com.appcasal.BuildConfig
import br.com.appcasal.dao.datasource.remote.*
import br.com.appcasal.dao.dto.network.CorporativeRetrofit
import br.com.appcasal.dao.repository.*
import br.com.appcasal.dao.service.*
import br.com.appcasal.domain.usecase.*
import br.com.appcasal.viewmodel.*
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

    single<ViagemService> {
        CorporativeRetrofit(get(), BuildConfig.BASE_URL).retrofit.create(
            ViagemService::class.java
        )
    }

    single<LugarVisitadoService> {
        CorporativeRetrofit(get(), BuildConfig.BASE_URL).retrofit.create(
            LugarVisitadoService::class.java
        )
    }

    single<GastoViagemService> {
        CorporativeRetrofit(get(), BuildConfig.BASE_URL).retrofit.create(
            GastoViagemService::class.java
        )
    }
}

val settingsDataSourceModule = module {
    single { MetaRemoteDataSource(get()) }
    single { TransacaoRemoteDataSource(get()) }
    single { ReceitaRemoteDataSource(get()) }
    single { IngredienteRemoteDataSource(get()) }
    single { ViagemRemoteDataSource(get()) }
    single { LugarVisitadoRemoteDataSource(get()) }
    single { GastoViagemRemoteDataSource(get()) }
}

val settingsRepositoryModule = module {
    single { MetaRepository(get()) }
    single { TransacaoRepository(get()) }
    single { ReceitaRepository(get()) }
    single { IngredienteRepository(get()) }
    single { ViagemRepository(get()) }
    single { LugarVisitadoRepository(get()) }
    single { GastoViagemRepository(get()) }
}

val settingsUseCaseModule = module {
    factory { GetMetasUseCase(get()) }
    factory { GetMetasByFilterUseCase(get()) }
    factory { InsereMetaUseCase(get()) }
    factory { AlteraMetaUseCase(get()) }
    factory { DeletaMetaUseCase(get()) }

    factory { GetTransacoesUseCase(get()) }
    factory { GetResumoUseCase(get()) }
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
    factory { GetIngredienteByReceitaUseCase(get()) }
    factory { MarcarDesmarcarIngredienteUseCase(get()) }
    factory { DesmarcarTodosIngredientesUseCase(get()) }

    factory { GetViagensUseCase(get()) }
    factory { InsereViagemUseCase(get()) }
    factory { AlteraViagemUseCase(get()) }
    factory { DeletaViagemUseCase(get()) }

    factory { GetLugarVisitadoByViagemUseCase(get()) }
    factory { InsereLugarVisitadoUseCase(get()) }
    factory { AlteraLugarVisitadoUseCase(get()) }
    factory { DeletaLugarVisitadoUseCase(get()) }

    factory { GetGastoViagemByViagemUseCase(get()) }
    factory { InsereGastoViagemUseCase(get()) }
    factory { AlteraGastoViagemUseCase(get()) }
    factory { DeletaGastoViagemUseCase(get()) }
}

val settingsViewModelModule = module {
    viewModel { ListaMetasViewModel(get(), get(), get(), get(), get()) }
    viewModel { ListaTransacoesViewModel(get(), get(), get(), get(), get(), get()) }
    viewModel { FormReceitasViewModel(get(), get(), get(), get(), get(), get()) }
    viewModel { DetalheReceitaViewModel(get(), get(), get()) }
    viewModel { ListaReceitasViewModel(get(), get()) }
    viewModel { ListaViagensViewModel(get(), get(), get(), get()) }
    viewModel { LugaresVisitadosViewModel(get(), get(), get(), get()) }
    viewModel { GastosViagemViewModel(get(), get(), get(), get()) }
}

val settingsKoinModule = settingsServiceModule +
        settingsDataSourceModule +
        settingsUseCaseModule +
        settingsRepositoryModule +
        settingsViewModelModule
