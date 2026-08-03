package id.co.drakorid.tv

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import id.co.drakorid.tv.data.TokenBootstrapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class DrakorTVApp : Application() {

    @Inject
    lateinit var tokenBootstrapper: TokenBootstrapper

    private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        // Fetch anonymous API token at startup so first screen loads instantly
        appScope.launch {
            tokenBootstrapper.ensureToken()
        }
    }
}
