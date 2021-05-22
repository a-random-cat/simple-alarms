package cat.simple.alarms.entities

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Alarm(
    @PrimaryKey
    var id: String = "",
    var name: String = "",
    var endTime: Long = 0
) : RealmObject()