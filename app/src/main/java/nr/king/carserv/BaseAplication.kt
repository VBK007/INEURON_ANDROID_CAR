package nr.king.carserv

import android.app.Application
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BaseAplication: Application() {

    override fun onCreate() {
        super.onCreate()

    }

}