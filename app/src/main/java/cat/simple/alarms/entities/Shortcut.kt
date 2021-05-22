package cat.simple.alarms.entities
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey


open class Shortcut(
    @PrimaryKey
    var id: String = "",
    var name: String = "",
    var icon: String? = null,
    var duration: Long = 0
) : RealmObject()