package elieomatuku.cineast_android.utils

interface ValueStore {
    fun set(key: String, value: String)

    fun get(key: String, fallback: String?): String?

    fun remove(key: String)
}