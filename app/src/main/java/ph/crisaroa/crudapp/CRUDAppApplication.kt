package ph.crisaroa.crudapp

import android.app.Application
import com.google.android.material.color.DynamicColors


class CRUDAppApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        DynamicColors.applyToActivitiesIfAvailable(this)
    }
}