package cat.simple.alarms

import java.util.*

class HelperCrypto {
    companion object {
        fun randomUUID(): String {
            return UUID.randomUUID().toString().replace("-", "")
        }
    }
}