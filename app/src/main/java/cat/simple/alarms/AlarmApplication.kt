package cat.simple.alarms

import android.app.Application
import cat.simple.alarms.entities.Shortcut
import io.realm.Realm
import io.realm.RealmConfiguration
import java.util.concurrent.TimeUnit

class AlarmApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)

        val config = RealmConfiguration.Builder()
            .initialData { r ->
                r.createObject(Shortcut::class.java, HelperCrypto.randomUUID()).apply {
                    name = "[Genshin] Parametric Transformer"
                    duration = TimeUnit.DAYS.toMillis(7)
                    icon = "ic_parametric"
                }
                r.createObject(Shortcut::class.java, HelperCrypto.randomUUID()).apply {
                    name = "[Genshin] Expedition 15"
                    duration = TimeUnit.HOURS.toMillis(15)
                    icon = "ic_expedition_15"
                }
                r.createObject(Shortcut::class.java, HelperCrypto.randomUUID()).apply {
                    name = "[Genshin] Expedition 20"
                    duration = TimeUnit.HOURS.toMillis(20)
                    icon = "ic_expedition_20"
                }
            }
            .deleteRealmIfMigrationNeeded()
            .build()
        Realm.setDefaultConfiguration(config)
    }
}