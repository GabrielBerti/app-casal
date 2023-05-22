package br.com.appcasal

import android.content.Context
import androidx.startup.Initializer
import br.com.appcasal.di.settingsKoinModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.Koin
import org.koin.core.context.startKoin
import org.koin.java.KoinJavaComponent.getKoin

internal class AppCasalInitializer : Initializer<Koin> {
    override fun create(context: Context): Koin {
        val koin = try {
            getKoin()
        } catch (e: IllegalStateException) {
            startKoin { androidContext(context) }.koin
        }
        koin.loadModules(settingsKoinModule)

        return koin
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> = mutableListOf()
}
